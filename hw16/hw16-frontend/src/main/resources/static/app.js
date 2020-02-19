// var stompClient = null;
//
// function sendUser() {
//
//     var user = {
//         'name': $("#name").val(),
//         'age': $("#age").val(),
//     };
//
//     var loc = window.location;
//     var url = '//' + loc.host + loc.pathname + '/websocket';
//     // var url = "/websocket";
//     console.log(url);
//
//     stompClient = Stomp.over(new SockJS(url));
//
//     stompClient.connect({}, frame => {
//         console.log(`Connected: ${frame}`);
//     });
//
//     setInterval(
//         stompClient.send("/app/addUser", {}, JSON.stringify({'messageStr': user})),
//         3000
//     );
//
//
//     stompClient.subscribe('/topic/response/addUser', function(response) {
//         var data = JSON.parse(response.body);
//         console.log("Responsed: " + data.message);
//         //alert(data.message);
//     });
// }
var stompClient = null;

var connect_callback = function() {
    var user = {
        'name': $("#name").val(),
        'age': $("#age").val(),
    };
    var loc = window.location;
    var url = loc.protocol + '//' + loc.host + loc.pathname + '/websocket';

    stompClient = Stomp.over(new SockJS(url));

    stompClient.connect({}, frame => {
        console.log(`1) Connected: ${frame}`);
        stompClient.send("/app/addUser", {}, JSON.stringify({'messageStr': user}));
        stompClient.subscribe('/topic/response/addUser', function(response) {
            var data = JSON.parse(response.body);
            console.log("2) Responsed: " + data.message);
        });
    });

};

function sendUser() {
    connect_callback();
}

$(() => {
    $("form").on('submit', event => event.preventDefault());
});