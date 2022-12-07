function validar() {
    var dnicliente = document.getElementById("formFactura:Nombre").value;
    console.log(dnicliente);
    if (dnicliente.length === 8) {
        console.log(dnicliente.length);
        PF('wbtnRegistrarVenta').enable()();
    } else {
        PF('wbtnRegistrarVenta').disable();
    }
}