package com.parque_industrial.dto.proyecto;

public record CrearRequestDefinitivoDTO(

        String usuarioNombre,
        String nombre,
        String descripcion,
        String actividadPrincipal,
        String actividadSecundaria,
        String telefono,
        String rubro,
        String descripcionServicio,
        String personaReferente,
        String materiasPrimas,
        String destinoProduccion,

        Double superficieRequerida,
        Double superficieTrabajo,
        Double superficieDeposito,
        Double superficieCubierta,
        Double superficieEstacionamiento,

        String tienePlanos,
        String linkPlanos,

        Double energiaRequerida,
        Integer personalAOcupar,
        String tensionAlimentacion,
        Double potenciaInstalada,
        Double aguaMensual,
        Double gasMensual,

        String residuosTipo,
        Double residuosCantidad,
        String tratamientoEfluentes,
        String tipoEmpresa,
        String direccion,
        String pretensionTraslado,
        String emplazamientoActual,
        String tiempoRadicacion,
        String balanzaPublica,
        String comedor,
        String sumCoworking,

        String cuitEmpresa,

        String linkViabilidadFinanciera,
        String linkEstudioMercado,
        String linkImpactoAmbiental,
        String linkHabilitacionMunicipal,
        String linkCertificadoInhibiciones

) {}