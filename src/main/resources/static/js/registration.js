
var user_section = document.getElementById("user-section");
var service_section = document.getElementById("service-section");

var select_service = document.getElementById("service");
var select_user = document.getElementById("user");

var accountType = registrationForm.accountType;

function selectPrivateUser(){
    service_section.style.display = "none";
    user_section.style.display = "block";
    document.getElementById("firstname").required  = true;
    document.getElementById("lastname").required  = true;
    document.getElementById("name").required  = false;
    document.getElementById("serviceType").required  = false;
}

function selectServicAccount(){
    user_section.style.display = "none";
    service_section.style.display = "block";
    document.getElementById("firstname").required  = false;
    document.getElementById("lastname").required  = false;
    document.getElementById("name").required  = true;
    document.getElementById("serviceType").required  = true;
}

function verifyForm(){
    let pass1 = document.getElementById("password").value;
    let pass2 = document.getElementById("passwordagain").value;

    let err_msg = document.getElementById("error_message");

    if(pass1 != pass2){
        err_msg.innerHTML = "Hasła nie są takie same!";
        return false;
    }

    let bithY = document.getElementById("birthyear");
    if(bithY.value == null || bithY.value == "")
        bithY.value = 0;

    return true;
}

window.onload = function(){
    if(accountType == 'PRIVATE'){
        selectPrivateUser();
    }else{
        selectServicAccount();
    }
}

select_user.addEventListener("click", selectPrivateUser)
select_service.addEventListener("click", selectServicAccount)


