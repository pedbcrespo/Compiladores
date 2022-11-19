package frontEndCompilador.dto

class DTOTipoDto {
    DTOToken dtoToken
    String tipo

    DTOTipoDto(DTOToken dtoToken, String tipo) {
        this.dtoToken = dtoToken
        this.tipo = tipo
    }
}
