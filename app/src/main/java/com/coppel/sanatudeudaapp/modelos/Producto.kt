package com.coppel.sanatudeudaapp.modelos

data class Producto (var descripcion: String?,
                     var descuento: Int?,
                     var idu_producto: Int?){

    constructor():this(
            "",
            0,
            0)

}