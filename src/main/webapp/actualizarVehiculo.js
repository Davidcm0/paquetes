var self;
function ViewModel() {
	self = this;
	var url = "ws://" + window.location.host + "/paquetes";
	self.sws = new WebSocket(url);


	self.crear = function() {
		var matricula = escapeHtml($('#matricula').val());
		matricula = stringEscape(matricula);
		var marca = escapeHtml($('#marca').val());
		marca = stringEscape(marca);
		var modelo = escapeHtml($('#modelo').val());
		modelo = stringEscape(modelo);
		var color = escapeHtml($('#color').val());
		color = stringEscape(color);	
		
		const info = {
			type: 'ActualizarVehiculo',
			matriculaVieja:sessionStorage.userName,
			matriculaNueva: matricula,
			marca: marca,
			modelo: modelo,
			color: color,
			success: function() {
				window.location.href = 'intranet.html';
				alert('Se ha creado correctamente');
				
			},
			error: function() {
				
				alert('Se ha creado incorrectamente');
			}
		};
		self.sws.send(JSON.stringify(info));
		if(matricula != ""){
			sessionStorage.userName = matricula;
		}
		window.location.href = 'intranet.html';
	};
	//estas dos funciones de abajo son para evitar ataques de SQL injection
	function escapeHtml(text) {
		  var map = {
		    '&': '&amp;',
		    '<': '&lt;',
		    '>': '&gt;',
		    '"': '&quot;',
		    "'": '&#039;'
		  };
		  
		  return text.replace(/[&<>"']/g, function(m) { return map[m]; });
		}
	function stringEscape(s) {
	    return s ? s.replace(/\\/g,'\\\\').replace(/\n/g,'\\n').replace(/\t/g,'\\t').replace(/\v/g,'\\v').replace(/'/g,"\\'").replace(/"/g,'\\"').replace(/[\x00-\x1F\x80-\x9F]/g,hex) : s;
	    function hex(c) { var v = '0'+c.charCodeAt(0).toString(16); return '\\x'+v.substr(v.length-2); }
	}

}
var vm = new ViewModel();
ko.applyBindings(vm);