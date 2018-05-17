var checking = document.getElementById('checking');
var savings = document.getElementById("savings");
var available = document.getElementById("available");

var checkingAmount = document.getElementById('checkingAmount');
var savingsAmount = document.getElementById("savingsAmount");

var error = document.getElementById("errorMessage");
var errorHeading = document.getElementById("errorHeading");
var errorDiv = document.getElementById("errorDiv");
errorDiv.style.display = "none";
errorHeading.innerHTML = "Error";
error.innerHTML = "You have requested to transfer more funds than you currently have. Please try again.";


$("a.tooltipLink").tooltip();
function addBalances(){
    errorDiv.style.display = "none";
    var data = this.response;
    checkingAmount.value = "";
    savingsAmount.value = "";
    // handle errors here
    if(data != null && this.status > 300){
       // console.log(this.status);
        errorDiv.style.display = "block";



        return;
    }
    errorHeading.innerHTML = "";
   // console.log(data);
    var dataArray = JSON.parse(data);

    checking.textContent = '$' + formatNumber(dataArray['checkingBalance']);
    savings.innerHTML = '$' + formatNumber(dataArray['savingsBalance']);
    var sum =  parseFloat(dataArray['checkingBalance']) +
                  parseFloat(dataArray['savingsBalance']);
    available.innerHTML = "$" + formatNumber(sum);

    history();

}



var xhr = new XMLHttpRequest();
    xhr.open("GET", "api/dashboard/");
    /*Preparing request body */
    xhr.addEventListener("load", addBalances)
    xhr.send();


function depositSavings(){
    var xhr = new XMLHttpRequest();
        xhr.open("POST", "api/savings/deposit");
        /*Preparing request body */
        xhr.addEventListener("load", addBalances)

        var amount = savingsAmount.value;
        if (amount === null || isNaN(amount) || amount =="") {
                alert("Please enter a valid amount to deposit.")
                return; //break out of the function early

            }

        var data = {};
        data['amount'] = amount;
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.send(JSON.stringify(data));
}
function depositChecking(){
    var xhr = new XMLHttpRequest();
        xhr.open("POST", "api/checking/deposit");
        /*Preparing request body */
        xhr.addEventListener("load", addBalances)

        var amount = checkingAmount.value;
         if (amount === null || isNaN(amount) || amount =="") {
         alert("Please enter a valid amount to deposit.")
                return; //break out of the function early
            }
        var data = {};
        data['amount'] = amount;
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.send(JSON.stringify(data));
}
function withdrawChecking(){
    var xhr = new XMLHttpRequest();
        xhr.open("POST", "api/checking/withdraw");
        /*Preparing request body */
        xhr.addEventListener("load", addBalances)

        var amount = checkingAmount.value;
         if (amount === null || isNaN(amount) || amount =="") {
         alert("Please enter a valid amount to withdraw.")
                return; //break out of the function early
            }
        var data = {};
        data['amount'] = amount;
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.send(JSON.stringify(data));

}
function withdrawSavings(){
    var xhr = new XMLHttpRequest();
        xhr.open("POST", "api/savings/withdraw");
        /*Preparing request body */
        xhr.addEventListener("load", addBalances)

        var amount = savingsAmount.value;
        if (amount === null || isNaN(amount) || amount =="") {
         alert("Please enter a valid amount to withdraw.")
                return; //break out of the function early
            }
        var data = {};
        data['amount'] = amount;
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.send(JSON.stringify(data));
}
function transferIntoSavings(){
var xhr = new XMLHttpRequest();
        xhr.open("POST", "api/transfer");
        /*Preparing request body */
        xhr.addEventListener("load", addBalances)

        var amount = checkingAmount.value;
        if (amount === null || isNaN(amount) || amount =="") {
         alert("Please enter a valid amount to transfer.")
                return; //break out of the function early
            }
        var data = {};
        data['amount'] = amount;
        data['to'] = "savings";
        data['from'] = "checking"
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.send(JSON.stringify(data));
}

function transferIntoChecking(){
var xhr = new XMLHttpRequest();
        xhr.open("POST", "api/transfer");
        /*Preparing request body */
        xhr.addEventListener("load", addBalances)

        var amount = savingsAmount.value;
        if (amount === null || isNaN(amount) || amount =="") {
         alert("Please enter a valid amount to transfer.")
                return; //break out of the function early
            }

        var data = {};
        data['amount'] = amount;
        data['from'] = "savings";
        data['to'] = "checking"
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.send(JSON.stringify(data));
}
$(function () {
  $('[data-toggle="popover"]').popover()
});

function history(){

var xhr = new XMLHttpRequest();
        xhr.open("GET", "api/history");
        /*Preparing request body */
        xhr.addEventListener("load", addHistory);
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.send();

}

function addHistory(){

    var data = this.response;
    var dataArray = JSON.parse(data);
    var historyDiv = document.getElementById("historyDiv");
    var table = document.getElementById("historyTable");
    while(table.firstChild){
        table.removeChild(table.firstChild);
    }

    var th = document.createElement("thead");
        var td1 = document.createElement("td");
        var td2 = document.createElement("td");
        var td3 = document.createElement("td");
        var td4 = document.createElement("td");
        var td5 = document.createElement("td");
        td1.innerHTML = "Into";
        td2.innerHTML = "From";
        td3.innerHTML = "Amount";
        td4.innerHTML = "Date";
        td5.innerHTML = "New Balance";
        var trow = document.createElement("tr");
        trow.appendChild(td1);
        trow.appendChild(td2);
        trow.appendChild(td3);
        trow.appendChild(td4);
        trow.appendChild(td5);
        th.appendChild(trow);
        table.appendChild(th);


    for(var i = 0; i < dataArray.length; i++){



        var trow = document.createElement("tr");
        var td1 = document.createElement("td");
        var td2 = document.createElement("td");
        var td3 = document.createElement("td");
        var td4 = document.createElement("td");
        var td5 = document.createElement("td");


        td1.innerHTML = dataArray[i]['accountTo'];
        td2.innerHTML = dataArray[i]['accountFrom'];
        td3.innerHTML = '$' + formatNumber(dataArray[i]['amount']);

        //console.log(moment(dataArray[i]['createdDatetime']));

        td4.innerHTML = moment(dataArray[i]['createdDatetime']).format('MMMM Do YYYY, h:mm:ss a');
        //td4.innerHTML = new Date(dataArray[i]['createdDatetime']);
        td5.innerHTML = '$' + formatNumber(dataArray[i]['balance']);

        trow.appendChild(td1);
        trow.appendChild(td2);
        trow.appendChild(td3);
        trow.appendChild(td4);
        trow.appendChild(td5);

        table.insertBefore(trow, table.firstChild);
    }


}

function formatNumber(num){
    return parseFloat(Math.round(num * 100) /100).toFixed(2);
}
function toggleHistory(){
    var table = document.getElementById("historyTable");
    table.setAttribute("class", "table table-striped table-dark");
    if(table.style.display == "none"){
    table.style.display = "table";
    }
    else{
        table.style.display = "none";
    }
}


