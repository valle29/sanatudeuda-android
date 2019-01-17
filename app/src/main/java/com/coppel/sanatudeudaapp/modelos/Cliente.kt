package com.coppel.sanatudeudaapp.modelos

class Cliente(var idu_cliente: Int? = null,
              var nombre: String? = null,
              var correo: String? = null,
              var contraseña: String? = null,
              var num_cliente: String? = null,
              var avatar: String? = null,
              var celular: String? = null,
              var telefono: String? = null,
              var fecha_alta: String? = null,
              var puntualidad: String? = null,
              var token: String? = null) {

    constructor():this(
            0,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "")

}