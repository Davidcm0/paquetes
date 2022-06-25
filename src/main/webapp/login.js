

let login = function() {
	const info = {
		type: 'Login',
		matricula: $('#matricula').val(),
		pwd: $('#password').val()
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