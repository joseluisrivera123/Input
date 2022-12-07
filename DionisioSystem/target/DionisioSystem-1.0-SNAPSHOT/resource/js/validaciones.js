function validarDNI(){
    let dni = document.getElementById("Regis:DNI").value;
    console.log(dni);
    if(dni.length === 8){
        PF('wbtnRegistrar').enable();
    }else{
        PF('wbtnRegistrar').disable();
    }
}

