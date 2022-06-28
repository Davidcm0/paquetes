var self;
function ViewModel() {
	self = this;
	self.listapedidosMios = ko.observableArray([]);
	self.listapedidosDisponibles = ko.observableArray([]);
	self.pedidosAsignados = ko.observableArray([]);
	self.pedidosEliminados = ko.observableArray([]);
	self.idPedido = ko.observable('');
	self.NuevaUbi = ko.observableArray([]);
	self.user = ko.observable('');
	//self.datos = [];
	//self.datos2 = [];
	//self.datos3 = [];
	//self.datos4 = [];
	this.usuario = sessionStorage.userName;
	var url = "ws://" + window.location.host + "/paquetes";
	self.sws = new WebSocket(url);
	
	self.sws.onopen = function(event) {
		self.user(sessionStorage.userName);
		var msg = {
			type: "ready",
			matricula: sessionStorage.userName,
			
		};
		self.sws.send(JSON.stringify(msg));
	};
	
	self.sws.onmessage = function(event) {


		var data = event.data;
		data = JSON.parse(data);
		var pedidos = data.pedidos;
		var vehiculo = data.pedidos[0];
		document.getElementById('marca').innerText = "Marca: " + vehiculo.Marca;
		document.getElementById('modelo').innerText = "Modelo: " + vehiculo.Modelo;
		document.getElementById('color').innerText = "Color: " +vehiculo.Color; 
		
		if(pedidos != null){
			for (var i = 1; i < pedidos.length; i++) {
				var pedido = pedidos[i];
				if(pedido.Vehiculo == sessionStorage.userName ){
					self.listapedidosMios.push(new Pedido(pedido.Id, pedido.Ubicaciones, pedido.Vehiculo));
				}else if(pedido.Vehiculo == ""){
					self.listapedidosDisponibles.push(new Pedido(pedido.Id, pedido.Ubicaciones, pedido.Vehiculo));
				}
				
			}
		}

		document.getElementsByTagName('h1')[0].innerText = sessionStorage.userName;

	}
	
	self.asignarPedido = function() {
		for (var i = 0; i < self.listapedidosDisponibles().length; i++) {
			if (document.getElementsByClassName("form-check-input 2")[i].checked === true) {
				self.pedidosAsignados.push(document.getElementsByClassName("form-check-label 2")[i].innerHTML);
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
	
	self.eliminarPedido = function() {
		for (var i = 0; i < self.listapedidosMios().length; i++) {
			if (document.getElementsByClassName("form-check-input")[i].checked === true) {
				self.pedidosEliminados.push(document.getElementsByClassName("form-check-label")[i].innerHTML);
			}
		}
		
		const info ={
				type: 'eliminar',
				matricula: sessionStorage.userName,
				pedidos: self.pedidosEliminados()
		};
		self.sws.send(JSON.stringify(info));
		location.reload();
	}
	
	self.actualizarUbicacion= function() {
	    var nueva_ubi = escapeHtml($('#actualizacionUbi').val());
		nueva_ubi = stringEscape(nueva_ubi);
		for (var i = 0; i < self.listapedidosMios().length; i++) {
			if (document.getElementsByClassName("form-check-input")[i].checked === true) {
				self.NuevaUbi.push(document.getElementsByClassName("form-check-label")[i].innerHTML);
			}
		}
		
		const info ={
				type: 'actualizarUbi',
				matricula: sessionStorage.userName,
				ubicacion: nueva_ubi,
				pedidos: self.NuevaUbi()
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
			
	
	class Pedido {
		constructor (id,ubicaciones,vehiculo) {
			this.id = id;
			this.ubicaciones = ubicaciones;
			this.vehiculo = vehiculo;
		}
		
	}

}
var vm = new ViewModel();
ko.applyBindings(vm);
