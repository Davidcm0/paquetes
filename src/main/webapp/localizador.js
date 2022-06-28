var self;
function ViewModel() {
	self = this;
	self.idPedido = ko.observable('');
	self.user = ko.observable('');
	self.matricula = ko.observable('');
	self.listaUbicaciones = ko.observableArray([]);
	this.usuario = sessionStorage.userName;
	var url = "ws://" + window.location.host + "/paquetes";
	self.sws = new WebSocket(url);
	
	self.sws.onopen = function(event) {
		
	};
	
	self.sws.onmessage = function(event) {
		var data = event.data;
		data = JSON.parse(data);
		var pedido = data.pedido[0];
		var vehiculo = data.pedido[1];
		document.getElementById('idpedido').innerText = pedido.Id;
		var ubicaciones = pedido.Ubicaciones;
		for (var i = 0; i < ubicaciones.length; i++) {
				var ubicacion = ubicaciones[i];
					self.listaUbicaciones.push(new Ubicacion(ubicacion));

			}
			
		document.getElementById('matricula').innerText = "Matr\u00EDcula: " + pedido.Vehiculo;
		document.getElementById('marca').innerText = "Marca: " + vehiculo.Marca;
		document.getElementById('modelo').innerText = "Modelo: " + vehiculo.Modelo;
		document.getElementById('color').innerText = "Color: " +vehiculo.Color;
		
	}
	


	self.localizarPedido = function() {	
		if($('#idpedido').val() != ""){
			const info = {
			type: 'localizar',
			IdPedido: parseInt($('#idpedido').val()),
			success: function() {
				alert('Se ha enviado correctamente');
			},
			error: function() {

				alert('Se ha creado incorrectamente');
			}
		};
		self.sws.send(JSON.stringify(info));
		}
		
	};
	
	self.asignarPedido = function() {
		for (var i = 0; i < self.listapedidos().length; i++) {
			if (document.getElementsByClassName("form-check-input")[i].checked === true) {
				self.pedidosAsignados.push(document.getElementsByClassName("form-check-label")[i].innerHTML);
			}
		}
		
		const info ={
				type: 'asignar',
				matricula: sessionStorage.userName,
				pedidos: self.pedidosAsignados()
		};
		self.sws.send(JSON.stringify(info));
		location.reload();
	}
	
	function escapeHtml(text) {
		  var map = {
		    '&': '&amp;',
		    '<': '&lt;',
		    '>': '&gt;',
		    '"': '&quot;',
		    "'": '&#039;',
		    '"\"': '&#92;'
		  };
		  
		  return text.replace(/[&<>"']/g, function(m) { return map[m]; });
		}
	function stringEscape(s) {
	    return s ? s.replace(/\\/g,'\\\\').replace(/\n/g,'\\n').replace(/\t/g,'\\t').replace(/\v/g,'\\v').replace(/'/g,"\\'").replace(/"/g,'\\"').replace(/[\x00-\x1F\x80-\x9F]/g,hex) : s;
	    function hex(c) { var v = '0'+c.charCodeAt(0).toString(16); return '\\x'+v.substr(v.length-2); }
	}
	
	function sleep(milliseconds) {
		  const date = Date.now();
		  let currentDate = null;
		  do {
		    currentDate = Date.now();
		  } while (currentDate - date < milliseconds);
		}
			

	
	class Ubicacion {
		constructor (ubicacion) {
			this.ubicacion = ubicacion;
		}

	}

}
var vm = new ViewModel();
ko.applyBindings(vm);
