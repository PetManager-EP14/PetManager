package com.ep14.pet_manager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ep14.pet_manager.dto.SaleDTO;
import com.ep14.pet_manager.dto.SaleDetailDTO;
import com.ep14.pet_manager.entity.Product;
import com.ep14.pet_manager.entity.Sale;
import com.ep14.pet_manager.entity.User;
import com.ep14.pet_manager.mapper.SaleMapper;
import com.ep14.pet_manager.repository.ProductRepository;
import com.ep14.pet_manager.repository.SaleRepository;
import com.ep14.pet_manager.repository.UserRepository;

class SaleServiceTest {

    @Mock
    private SaleRepository saleRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleService service;

    private User user;
    private Product product;
    private Sale sale;
    private SaleDTO saleDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(UUID.randomUUID());

        product = new Product();
        product.setProductId(1L);
        product.setName("Collar");
        product.setStock(BigDecimal.valueOf(10));
        product.setPriceSale(BigDecimal.valueOf(10000));

        sale = new Sale();
        sale.setSaleId(1L);
        sale.setUser(user);
        sale.setSaleDetails(new ArrayList<>());
        sale.setTotal(BigDecimal.ZERO);

        saleDTO = new SaleDTO();
        saleDTO.setUserId(user.getUserId());

        SaleDetailDTO detail = new SaleDetailDTO();
        detail.setProductId(1L);
        detail.setAmount(BigDecimal.ONE);

        saleDTO.setDetails(List.of(detail));
    }

    // Caso 1: registro exitoso de venta
    @Test
    void registerSale_successful() {
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        when(productRepo.findById(any())).thenReturn(Optional.of(product));
        when(saleRepo.saveAndFlush(any(Sale.class))).thenReturn(sale);
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        SaleDTO result = service.registerSale(saleDTO);

        assertThat(result).isNotNull();
        verify(saleRepo, times(2)).saveAndFlush(any(Sale.class));
        verify(productRepo, times(1)).save(any(Product.class));
    }

    // Caso 2: usuario no encontrado
    @Test
    void registerSale_whenUserNotFound_shouldThrowException() {
        when(userRepo.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.registerSale(saleDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    // Caso 3: producto no encontrado
    @Test
    void registerSale_whenProductNotFound_shouldThrowException() {
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        when(productRepo.findById(any())).thenReturn(Optional.empty());
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleRepo.saveAndFlush(any(Sale.class))).thenReturn(sale);

        assertThatThrownBy(() -> service.registerSale(saleDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Producto no encontrado");
    }

    // Caso 4: stock insuficiente
    @Test
    void registerSale_whenStockInsufficient_shouldThrowException() {
        product.setStock(BigDecimal.ZERO);
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        when(productRepo.findById(any())).thenReturn(Optional.of(product));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        when(saleRepo.saveAndFlush(any(Sale.class))).thenReturn(sale);
        assertThatThrownBy(() -> service.registerSale(saleDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    // Caso 5: obtener todas las ventas
    @Test
    void getAllSales_shouldReturnList() {
        when(saleRepo.findAll()).thenReturn(List.of(sale));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        List<SaleDTO> result = service.getAllSales();
        assertThat(result).isNotEmpty();
        verify(saleRepo).findAll();
    }

    // Caso 6: obtener venta por ID existente
    @Test
    void getSaleById_shouldReturnSale() {
        when(saleRepo.findById(1L)).thenReturn(Optional.of(sale));
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);
        SaleDTO result = service.getSaleById(1L);
        assertThat(result).isEqualTo(saleDTO);
    }

    // Caso 7: obtener venta por ID inexistente
    @Test
    void getSaleById_notFound_shouldThrowException() {
        when(saleRepo.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getSaleById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Venta no encontrada");
    }

    // Caso 8: ventas por usuario con resultados
    @Test
    void getSalesByUser_shouldReturnList() {
        when(saleRepo.findByUser_UserId(any())).thenReturn(List.of(sale));
        when(saleMapper.toEntity(any(SaleDTO.class))).thenReturn(sale);
        List<SaleDTO> result = service.getSalesByUser(user.getUserId());
        assertThat(result).hasSize(1);
    }

    // Caso 9: ventas por usuario sin resultados
    @Test
    void getSalesByUser_empty_shouldThrowException() {
        when(saleRepo.findByUser_UserId(any())).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> service.getSalesByUser(user.getUserId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El usuario no tiene ventas registradas");
    }

    @Test
    void getAllSalesFiltered_withSaleId_shouldReturnList() {
        when(saleRepo.findById(1L)).thenReturn(Optional.of(sale));
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        List<SaleDTO> result = service.getAllSalesFiltered(null, null, null, 1L);

        assertThat(result).isNotEmpty();
        verify(saleRepo).findById(1L);
    }

    @Test
    void getAllSalesFiltered_withUserAndDates_shouldReturnList() {
        UUID userId = UUID.randomUUID();
        when(saleRepo.findByUserAndDateRange(eq(userId), any(), any())).thenReturn(List.of(sale));
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        List<SaleDTO> result = service.getAllSalesFiltered(userId, "2024-10-01T00:00:00Z", "2024-10-19T00:00:00Z", null);

        assertThat(result).hasSize(1);
        verify(saleRepo).findByUserAndDateRange(eq(userId), any(), any());
    }

    @Test
    void getAllSalesFiltered_withUserOnly_shouldReturnList() {
        UUID userId = UUID.randomUUID();
        when(saleRepo.findByUser_UserId(userId)).thenReturn(List.of(sale));
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        List<SaleDTO> result = service.getAllSalesFiltered(userId, null, null, null);

        assertThat(result).hasSize(1);
        verify(saleRepo).findByUser_UserId(userId);
    }

    @Test
    void getAllSalesFiltered_withDatesOnly_shouldReturnList() {
        when(saleRepo.findByDateBetween(any(), any())).thenReturn(List.of(sale));
        when(saleMapper.toDTO(any(Sale.class))).thenReturn(saleDTO);

        List<SaleDTO> result = service.getAllSalesFiltered(null, "2024-10-01T00:00:00Z", "2024-10-19T00:00:00Z", null);

        assertThat(result).hasSize(1);
        verify(saleRepo).findByDateBetween(any(), any());
    }
}
