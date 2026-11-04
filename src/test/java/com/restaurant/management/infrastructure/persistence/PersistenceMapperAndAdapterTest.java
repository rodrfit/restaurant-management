package com.restaurant.management.infrastructure.persistence;

import com.restaurant.management.domain.model.Address;
import com.restaurant.management.domain.model.MenuItem;
import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.infrastructure.persistence.adapter.MenuItemRepositoryAdapter;
import com.restaurant.management.infrastructure.persistence.adapter.RestaurantRepositoryAdapter;
import com.restaurant.management.infrastructure.persistence.adapter.UserRepositoryAdapter;
import com.restaurant.management.infrastructure.persistence.adapter.UserTypeRepositoryAdapter;
import com.restaurant.management.infrastructure.persistence.entity.MenuItemEntity;
import com.restaurant.management.infrastructure.persistence.entity.RestaurantEntity;
import com.restaurant.management.infrastructure.persistence.entity.UserEntity;
import com.restaurant.management.infrastructure.persistence.entity.UserTypeEntity;
import com.restaurant.management.infrastructure.persistence.mapper.AddressMapper;
import com.restaurant.management.infrastructure.persistence.mapper.MenuItemMapper;
import com.restaurant.management.infrastructure.persistence.mapper.RestaurantMapper;
import com.restaurant.management.infrastructure.persistence.mapper.UserMapper;
import com.restaurant.management.infrastructure.persistence.mapper.UserTypeMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class PersistenceMapperAndAdapterTest {

    @Test
    void testMappersWithNullAndValidValues() {
        assertNull(AddressMapper.toDomain(null));
        assertNull(AddressMapper.toEntity(null));
        assertNull(UserTypeMapper.toDomain(null));
        assertNull(UserTypeMapper.toEntity(null));
        assertNull(UserMapper.toDomain(null));
        assertNull(UserMapper.toEntity(null));
        assertNull(RestaurantMapper.toDomain(null));
        assertNull(RestaurantMapper.toEntity(null));
        assertNull(MenuItemMapper.toDomain(null));
        assertNull(MenuItemMapper.toEntity(null));

        Address address = new Address();
        address.setStreet("Rua 1");
        address.setNumber("10");
        address.setCity("SP");
        address.setZipCode("01000-000");

        UserType userType = new UserType();
        userType.setId(1L);
        userType.setName("Cliente");
        userType.setObservation("Obs");

        User user = new User();
        user.setId(2L);
        user.setName("João");
        user.setEmail("joao@email.com");
        user.setLogin("joao");
        user.setPassword("hash");
        user.setLastChange(LocalDateTime.now());
        user.setAddress(address);
        user.setUserType(userType);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(3L);
        restaurant.setNome("Restaurante Central");
        restaurant.setEndereco("Av. Brasil");
        restaurant.setTipoCozinha("Italiana");
        restaurant.setHorarioFuncionamento("10h-22h");
        restaurant.setOwner(user);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(4L);
        menuItem.setNome("Pizza");
        menuItem.setDescricao("Mussarela");
        menuItem.setPreco(45.0);
        menuItem.setDisponivelNoLocal(true);
        menuItem.setFotoPath("/img/pizza.png");
        menuItem.setRestaurant(restaurant);

        MenuItemEntity entity = MenuItemMapper.toEntity(menuItem);
        assertNotNull(entity);
        MenuItem roundTrip = MenuItemMapper.toDomain(entity);
        assertEquals(4L, roundTrip.getId());
        assertEquals("Pizza", roundTrip.getNome());
        assertEquals("Restaurante Central", roundTrip.getRestaurant().getNome());
        assertEquals("João", roundTrip.getRestaurant().getOwner().getName());
        assertEquals("Rua 1", roundTrip.getRestaurant().getOwner().getAddress().getStreet());
        assertEquals("Cliente", roundTrip.getRestaurant().getOwner().getUserType().getName());
    }

    @Test
    void testUserRepositoryAdapter() {
        JpaUserRepository jpaRepo = Mockito.mock(JpaUserRepository.class);
        UserRepositoryAdapter adapter = new UserRepositoryAdapter(jpaRepo);

        User domain = new User();
        domain.setId(1L);
        domain.setName("João");
        UserEntity entity = UserMapper.toEntity(domain);

        Mockito.when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(jpaRepo.findAll()).thenReturn(List.of(entity));
        Mockito.when(jpaRepo.save(any(UserEntity.class))).thenReturn(entity);
        Mockito.when(jpaRepo.existsById(1L)).thenReturn(true);
        Mockito.when(jpaRepo.existsByEmail("a@b.com")).thenReturn(true);
        Mockito.when(jpaRepo.findByNameContainingIgnoreCase("João")).thenReturn(List.of(entity));
        Mockito.when(jpaRepo.findByLogin("joao")).thenReturn(Optional.of(entity));
        Mockito.when(jpaRepo.existsByNameIgnoreCase("João")).thenReturn(true);

        assertTrue(adapter.findById(1L).isPresent());
        assertEquals(1, adapter.findAll().size());
        assertEquals(1L, adapter.save(domain).getId());
        adapter.delete(domain);
        adapter.deleteById(1L);
        assertTrue(adapter.existsById(1L));
        assertTrue(adapter.existsByEmail("a@b.com"));
        assertEquals(1, adapter.findByNameContainingIgnoreCase("João").size());
        assertTrue(adapter.findByLogin("joao").isPresent());
        assertEquals(1, adapter.findByNameIgnoreCase("João").size());
        assertTrue(adapter.existsByNameIgnoreCase("João"));
    }

    @Test
    void testUserTypeRepositoryAdapter() {
        JpaUserTypeRepository jpaRepo = Mockito.mock(JpaUserTypeRepository.class);
        UserTypeRepositoryAdapter adapter = new UserTypeRepositoryAdapter(jpaRepo);

        UserType domain = new UserType();
        domain.setId(1L);
        domain.setName("Cliente");
        UserTypeEntity entity = UserTypeMapper.toEntity(domain);

        Mockito.when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(jpaRepo.findAll()).thenReturn(List.of(entity));
        Mockito.when(jpaRepo.save(any(UserTypeEntity.class))).thenReturn(entity);
        Mockito.when(jpaRepo.existsById(1L)).thenReturn(true);
        Mockito.when(jpaRepo.existsByNameIgnoreCase("Cliente")).thenReturn(true);
        Mockito.when(jpaRepo.findByNameContainingIgnoreCase("Cli")).thenReturn(List.of(entity));
        Mockito.when(jpaRepo.findByNameIgnoreCase("Cliente")).thenReturn(Optional.of(entity));

        assertTrue(adapter.findById(1L).isPresent());
        assertEquals(1, adapter.findAll().size());
        assertEquals(1L, adapter.save(domain).getId());
        adapter.deleteById(1L);
        assertTrue(adapter.existsById(1L));
        assertTrue(adapter.existsByNameIgnoreCase("Cliente"));
        assertEquals(1, adapter.findByNameContainingIgnoreCase("Cli").size());
        assertTrue(adapter.findByNameIgnoreCase("Cliente").isPresent());
    }

    @Test
    void testRestaurantRepositoryAdapter() {
        JpaRestaurantRepository jpaRepo = Mockito.mock(JpaRestaurantRepository.class);
        RestaurantRepositoryAdapter adapter = new RestaurantRepositoryAdapter(jpaRepo);

        Restaurant domain = new Restaurant();
        domain.setId(1L);
        domain.setNome("Bistrô");
        RestaurantEntity entity = RestaurantMapper.toEntity(domain);

        Mockito.when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(jpaRepo.findAll()).thenReturn(List.of(entity));
        Mockito.when(jpaRepo.save(any(RestaurantEntity.class))).thenReturn(entity);
        Mockito.when(jpaRepo.existsById(1L)).thenReturn(true);
        Mockito.when(jpaRepo.existsByOwnerId(1L)).thenReturn(true);
        Mockito.when(jpaRepo.findByNomeContainingIgnoreCase("Bis")).thenReturn(List.of(entity));

        assertTrue(adapter.findById(1L).isPresent());
        assertEquals(1, adapter.findAll().size());
        assertEquals(1L, adapter.save(domain).getId());
        adapter.deleteById(1L);
        assertTrue(adapter.existsById(1L));
        assertTrue(adapter.existsByOwnerId(1L));
        assertEquals(1, adapter.findByNameContainingIgnoreCase("Bis").size());
        assertTrue(adapter.findByNameIgnoreCase("bistrô").isPresent());
    }

    @Test
    void testMenuItemRepositoryAdapter() {
        JpaMenuItemRepository jpaRepo = Mockito.mock(JpaMenuItemRepository.class);
        MenuItemRepositoryAdapter adapter = new MenuItemRepositoryAdapter(jpaRepo);

        MenuItem domain = new MenuItem();
        domain.setId(1L);
        domain.setNome("Pizza");
        MenuItemEntity entity = MenuItemMapper.toEntity(domain);

        Mockito.when(jpaRepo.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(jpaRepo.findAll()).thenReturn(List.of(entity));
        Mockito.when(jpaRepo.save(any(MenuItemEntity.class))).thenReturn(entity);
        Mockito.when(jpaRepo.existsById(1L)).thenReturn(true);
        Mockito.when(jpaRepo.findByNomeContainingIgnoreCase("Piz")).thenReturn(List.of(entity));

        assertTrue(adapter.findById(1L).isPresent());
        assertEquals(1, adapter.findAll().size());
        assertEquals(1L, adapter.save(domain).getId());
        adapter.deleteById(1L);
        assertTrue(adapter.existsById(1L));
        assertEquals(1, adapter.findByNameContainingIgnoreCase("Piz").size());
        assertTrue(adapter.findByNameIgnoreCase("pizza").isPresent());
    }
}
