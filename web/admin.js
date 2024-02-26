/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function removeMessage() {
    document.getElementById("message-box").classList.add("hidden")
}

function addPrompt() {    
    document.getElementById("optionsPrompt").classList.add("hidden")
    document.getElementById("addPrompt").classList.remove("hidden")
}

function updatePrompt() {
    document.getElementById("optionsPrompt").classList.add("hidden")
    document.getElementById("updatePrompt").classList.remove("hidden")
}

function deletePrompt() {
    document.getElementById("optionsPrompt").classList.add("hidden")
    document.getElementById("deletePrompt").classList.remove("hidden")
}

function cancel() {  
    document.getElementById("addPrompt").classList.add("hidden")
    document.getElementById("updatePrompt").classList.add("hidden")
    document.getElementById("deletePrompt").classList.add("hidden")
    document.getElementById("optionsPrompt").classList.remove("hidden")
}
