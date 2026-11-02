package com.restaurant.management.service.impl;

import org.springframework.stereotype.Service;
import com.restaurant.management.service.RestaurantService;
import com.restaurant.management.repository.RestaurantRepository;
import com.restaurant.management.repository.UserRepository;
import com.restaurant.management.dto.request.CreateRestaurantRequest;
import com.restaurant.management.dto.response.RestaurantResponse;
import com.restaurant.management.model.Restaurant;
import com.restaurant.management.model.User;
import com.restaurant.management.exception.UserNotFoundException;
import com.restaurant.management.exception.RestaurantAlreadyExistsException;
import com.restaurant.management.exception.RestaurantNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository repository;
    private final UserRepository userRepository;

    public RestaurantServiceImpl(RestaurantRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public RestaurantResponse create(CreateRestaurantRequest request) {
        if (request.ownerId() != null && repository.existsByOwnerId(request.ownerId())) {
            throw new RestaurantAlreadyExistsException();
        }

        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new UserNotFoundException(request.ownerId()));

        Restaurant restaurant = new Restaurant();
        restaurant.setNome(request.nome());
        restaurant.setEndereco(request.endereco());
        restaurant.setTipoCozinha(request.tipoCozinha());
        restaurant.setHorarioFuncionamento(request.horarioFuncionamento());
        restaurant.setOwner(owner);

        Restaurant saved = repository.save(restaurant);
        return toResponse(saved);
    }

    @Override
    public List<RestaurantResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponse findById(Long id) {
        Restaurant r = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        return toResponse(r);
    }

    @Override
    public RestaurantResponse update(Long id, CreateRestaurantRequest request) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        restaurant.setNome(request.nome());
        restaurant.setEndereco(request.endereco());
        restaurant.setTipoCozinha(request.tipoCozinha());
        restaurant.setHorarioFuncionamento(request.horarioFuncionamento());

        if (request.ownerId() != null && (restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(request.ownerId()))) {
            if (repository.existsByOwnerId(request.ownerId())) {
                throw new RestaurantAlreadyExistsException();
            }
            User owner = userRepository.findById(request.ownerId())
                    .orElseThrow(() -> new UserNotFoundException(request.ownerId()));
            restaurant.setOwner(owner);
        }

        Restaurant saved = repository.save(restaurant);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private RestaurantResponse toResponse(Restaurant saved) {
        Long ownerId = saved.getOwner() != null ? saved.getOwner().getId() : null;
        return new RestaurantResponse(saved.getId(), saved.getNome(), saved.getEndereco(),
                saved.getTipoCozinha(), saved.getHorarioFuncionamento(), ownerId);
    }
}
