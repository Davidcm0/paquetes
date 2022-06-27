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
	
	self.modificar = function() {
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
			

	
	class Ubicacion {
		constructor (ubicacion) {
			this.ubicacion = ubicacion;
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
