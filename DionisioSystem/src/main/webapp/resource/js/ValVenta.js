function validar() {
    var dnicliente = document.getElementById("formFactura:Nombre").value;
    console.log(dnicliente);
    console.log(dnicliente.length);
    if (dnicliente.length === 8) {
        PF('wbtnRegistrarVenta').enable()();
    } else {
        PF('wbtnRegistrarVenta').disable();
    }
}
