package com.restaurant.management.domain.exception;

public final class ExceptionMessages {

    private ExceptionMessages() {}

    public static final String NAME_REQUIRED = "Nome é obrigatório";
    public static final String NAME_MAX_LENGTH = "Nome deve ter no máximo 255 caracteres";

    public static final String ADDRESS_REQUIRED = "Endereço é obrigatório";
    public static final String ADDRESS_MAX_LENGTH = "Endereço deve ter no máximo 255 caracteres";

    public static final String CUISINE_REQUIRED = "Tipo de cozinha é obrigatório";
    public static final String CUISINE_MAX_LENGTH = "Tipo de cozinha deve ter no máximo 255 caracteres";

    public static final String OPENING_HOURS_REQUIRED = "Horário de funcionamento é obrigatório";
    public static final String OPENING_HOURS_MAX_LENGTH = "Horário de funcionamento deve ter no máximo 255 caracteres";

    public static final String OWNER_ID_REQUIRED = "ID do proprietário é obrigatório";

    public static final String DESCRIPTION_REQUIRED = "Descrição é obrigatória";
    public static final String DESCRIPTION_MAX_LENGTH = "Descrição deve ter no máximo 1000 caracteres";

    public static final String PRICE_REQUIRED = "Preço é obrigatório";
    public static final String PRICE_GREATER_THAN_ZERO = "Preço deve ser maior que zero";

    public static final String AVAILABILITY_REQUIRED = "Disponibilidade no local é obrigatória";
    public static final String PHOTO_PATH_MAX_LENGTH = "Caminho da foto deve ter no máximo 255 caracteres";
    public static final String RESTAURANT_ID_REQUIRED = "ID do restaurante é obrigatório";

    public static final String EMAIL_INVALID = "E-mail inválido";
    public static final String EMAIL_REQUIRED = "E-mail é obrigatório";
    public static final String EMAIL_MAX_LENGTH = "E-mail deve ter no máximo 255 caracteres";

    public static final String LOGIN_REQUIRED = "Login é obrigatório";
    public static final String LOGIN_MAX_LENGTH = "Login deve ter no máximo 255 caracteres";

    public static final String PASSWORD_REQUIRED = "Senha é obrigatória";
    public static final String PASSWORD_LENGTH = "Senha deve ter entre 6 e 72 caracteres";

    public static final String TYPE_REQUIRED = "Tipo é obrigatório";

    public static final String USER_TYPE_NAME_REQUIRED = "Nome do tipo de usuário é obrigatório";
    public static final String USER_TYPE_NAME_MAX_LENGTH = "Nome do tipo de usuário deve ter no máximo 255 caracteres";
    public static final String USER_TYPE_OBSERVATION_MAX_LENGTH = "Observação do tipo de usuário deve ter no máximo 1000 caracteres";

    public static final String CURRENT_PASSWORD_REQUIRED = "Senha atual é obrigatória";
    public static final String NEW_PASSWORD_REQUIRED = "Nova senha é obrigatória";
    public static final String NEW_PASSWORD_LENGTH = "Nova senha deve ter entre 6 e 72 caracteres";

    public static final String INVALID_CURRENT_PASSWORD = "Senha atual inválida";
    public static final String NEW_PASSWORD_SAME_AS_CURRENT = "A nova senha deve ser diferente da senha atual";
    public static final String INVALID_LOGIN_CREDENTIALS = "Login ou senha inválidos";
    public static final String LOGIN_SUCCESS = "Login válido";
}