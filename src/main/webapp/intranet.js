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
	


	self.enviar = function() {	
		
		const info = {
			type: 'sendProyecto',
			usuario: sessionStorage.userName,
			success: function() {
				alert('Se ha enviar correctamente');
			},
			error: function() {

				alert('Se ha creado incorrectamente');
			}
		};
		self.sws.send(JSON.stringify(info));
	};
	
	self.explorador = function(){
		const info ={
				type: 'explorador',
		};
		self.sws.send(JSON.stringify(info));
	};
	
	self.excel = function() {
		const info ={
				type: 'excel',
				proyecto: self.nombreProyecto,
				usuario: sessionStorage.userName
		};
		self.sws.send(JSON.stringify(info));
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
	
	self.modificarVehiculo = function() {//cambiar
		var dut;
		var repeticiones;
		
		if(sessionStorage.userName != "admin"){
			var descripcion = escapeHtml(document.getElementById("descripcion").value);
			descripcion = stringEscape(descripcion);
			var new_nombre = escapeHtml( document.getElementById("nombreProyecto").value);
			new_nombre = stringEscape(new_nombre);
			if(document.getElementById("dut").value === ""){
				dut = 000;
			} else{
				dut =  document.getElementById("dut").value;
			}
			
			if(document.getElementById("repeticiones").value === ""){
				repeticiones = 000;
			} else{
				repeticiones =  document.getElementById("repeticiones").value;
			}
		var p = {
			type: "modificar",
			nombre: self.nombreProyecto(),
			new_nombre: new_nombre,
			descripcion: descripcion,
			dut: dut,
			repeticiones: repeticiones,
			lenguaje: document.getElementById("lenguaje").options[document.getElementById("lenguaje").selectedIndex].text,
			success: function() {
				location.reload();
			}
		};
		} else{
			var estado = document.getElementById("Estado").options[document.getElementById("Estado").selectedIndex].text;
			var mensaje = "The status of your project " + self.nombreProyecto() + " has changed from " + self.estadoProyecto() + " to " + estado + ".";
			var url = document.getElementById("url").value;
			if(estado != "Choose..."){
				if(estado === "En preparacion"){
					mensaje = mensaje + " This is because the code was not right, so try again or contact to dcarretero.1999@gmail.com, go to check it!";
				} else if(estado === "Medido"){
					mensaje = mensaje + " You can go to check the results!\n Here we give you the url of results: " + url + " and the password: " + document.getElementById("password").value;
				} else {
					mensaje = mensaje + " You have to wait for the measurement of your code.";
				}
				emailjs.init("user_kJwC21oweSb6llxLRUyRT");
				emailjs.send("service_zvob6fj","template_m06v46e",{
					from_name: "David",
					to_name: self.AutorProyecto(),
					message: mensaje,
					email_to: self.email_userProyecto(),
					});
			}

			sleep(2000);

			var p = {
					type: "estado",
					proyecto: self.nombreProyecto(),
					estado: estado,
					url: url,
					password: document.getElementById("password").value,
					success: function() {
						location.reload();
					}
				};
		}
		
		self.sws.send(JSON.stringify(p));
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
		
		info() {
			self.nombreProyecto(this.nombre);
			self.AutorProyecto(this.autor);
			self.email_userProyecto(this.email_user);
			self.estadoProyecto(this.estado);
			var p = {
				type: "info",
				nombre: this.nombre,
				user:sessionStorage.userName
				
			};
			
			self.sws.send(JSON.stringify(p));

		}
		
		terminar() {
			var p = {
				type: "terminar",
				proyecto: this.nombre
			};
			
			self.sws.send(JSON.stringify(p));
			location.reload();
		}
		

		resultados(){
			
			self.nombreProyecto(this.nombre);
			var p = {
				type: 'resultados',
				proyecto: this.nombre,
				usuario:sessionStorage.userName

			};
			// self.nombreUsuario(this.name);
			self.sws.send(JSON.stringify(p));
		}
		
		url_excel(){
			//location.href= this.url;
			window.open(this.url, '_blank');
		}
		
		estado_proyecto() {
			var matches = document.querySelectorAll('select');
			var p = {
				type: "estado",
				estado: document.getElementById("Estado").options[document.getElementById("Estado").selectedIndex].text,
				proyecto: this.nombre
			};
			
			self.sws.send(JSON.stringify(p));

		}

		enviar() {	
			
			const info = {
				type: 'sendProyecto',
				usuario: sessionStorage.userName,
				nombre: this.nombre,
				success: function() {
					alert('Se ha enviar correctamente');
				},
				error: function() {

					alert('Se ha creado incorrectamente');
				}
			};
			self.sws.send(JSON.stringify(info));
			location.reload();
		};

	}

}
var vm = new ViewModel();
ko.applyBindings(vm);
