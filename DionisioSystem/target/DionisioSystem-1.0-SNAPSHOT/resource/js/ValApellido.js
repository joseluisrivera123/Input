function ValidarApellido(){
    var apellido = String(document.getElementById('Regis:apellido').value).trim();
    var spaceCount = apellido.split(" ").length;
    console.log("Soy el space : " + spaceCount);
    
    var ape1="" , ape2="" , cadena="" ;
    cadena = apellido.split(" ");
    ape1 = cadena[0];
    ape2 = cadena[1];
   
    console.log("Ape1 --> " + ape1 + "Ape2 -->" + ape2);
    console.log("Ape1 --> " + ape1);
    console.log("Ape2 -->" + ape2);
    console.log(cadena);
    if(spaceCount === 2 && ape1.length > 1 && ape2.length > 1 ){
        PF('wbtnRegistrar').enable();
    }else{
        PF('wbtnRegistrar').disable();
    }
}


