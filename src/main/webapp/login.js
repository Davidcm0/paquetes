

let login = function() {
	const info = {
		type: 'Login',
		pwd: $('#matricula').val()
	};
	sessionStorage.userName = $('#matricula').val();
	const data = {
		data: JSON.stringify(info),
		url: 'login',
		type: 'post',
		contentType: 'application/json',
		success: function() {
			sessionStorage.userName = $('#matricula').val();
			window.location.href = "intranet.html";
		},
		error: function(response) {
			alert('LOGIN INCORRECTO');
		}
	};
	$.ajax(data);
};