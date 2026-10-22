package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.SaveRestaurantCommand;
import com.restaurant.management.application.service.RestaurantService;
import com.restaurant.management.domain.exception.RestaurantNotFoundException;
import com.restaurant.management.domain.exception.UserNotFoundException;
import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.repository.RestaurantRepository;
import com.restaurant.management.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll();
    }

    @Override
    public Restaurant findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    @Transactional
    @Override
    public Restaurant create(SaveRestaurantCommand command) {
        User owner = userRepository.findById(command.ownerId())
                .orElseThrow(() -> new UserNotFoundException(command.ownerId()));

        Restaurant restaurant = new Restaurant();
        restaurant.setNome(command.nome());
        restaurant.setEndereco(command.endereco());
        restaurant.setTipoCozinha(command.tipoCozinha());
        restaurant.setHorarioFuncionamento(command.horarioFuncionamento());
        restaurant.setOwner(owner);
        return repository.save(restaurant);
    }

    @Transactional
    @Override
    public Restaurant update(Long id, SaveRestaurantCommand command) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        restaurant.setNome(command.nome());
        restaurant.setEndereco(command.endereco());
        restaurant.setTipoCozinha(command.tipoCozinha());
        restaurant.setHorarioFuncionamento(command.horarioFuncionamento());

        if (Objects.nonNull(command.ownerId())
                && (Objects.isNull(restaurant.getOwner()) || !restaurant.getOwner().getId().equals(command.ownerId()))) {
            User owner = userRepository.findById(command.ownerId())
                    .orElseThrow(() -> new UserNotFoundException(command.ownerId()));
            restaurant.setOwner(owner);
        }

        return repository.save(restaurant);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
