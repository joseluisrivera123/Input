function validarRUC(){
    let ruc = document.getElementById("Regist:RUC").value;
    console.log(ruc);
    if(ruc.length === 11){
        PF('wbtnRegistrar').enable();
    }else{
        PF('wbtnRegistrar').disable();
    }
}

