var self;
function ViewModel() {
	self = this;
	self.listapedidos = ko.observableArray([]);
	self.pedidosAsignados = ko.observableArray([]);
	self.idPedido = ko.observable('');
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
		 
		
		if(pedidos != null){
			for (var i = 0; i < pedidos.length; i++) {
				var pedido = pedidos[i];
					self.listapedidos.push(new Pedido(pedido.Id, pedido.Ubicaciones, pedido.Vehiculo));
			}
		}
        //document.getElementById('DUTMin').innerText = resultados[0].DUT[0];
			//document.getElementById('DUTMax').innerText = resultados[0].DUT[1];
			//document.getElementById('DUTMedia').innerText = resultados[0].DUT[2];
			//document.getElementById('DUTQ1').innerText = resultados[0].DUT[3];
			//document.getElementById('DUTQ3').innerText = resultados[0].DUT[4];
			
			//document.getElementById('Pro').innerText = self.nombreProyecto();
			// self.time(resultados[0].DUT[0]) ;
		
		
		if(proyectos != null){
			for (var i = 0; i < proyectos.length; i++) {
				var proyecto = proyectos[i];
					self.listaproyectos.push(new Proyecto(proyecto.Nombre, proyecto.Descripcion, proyecto.Autor, proyecto.email_user, proyecto.Fecha, proyecto.DUT, proyecto.Repeticiones, proyecto.Lenguaje, proyecto.estado, proyecto.proyecto_enviado, proyecto.url, proyecto.password));
					
				

			}
			for(var i = 0; i < proyectos.length; i++){
				var proyecto = proyectos[i];
				if(proyecto.estado == "Medido"){
					document.getElementsByClassName("cards")[0].children[i].children[0].children[0].children[1].style.backgroundColor = "springgreen";
					//document.getElementsByClassName("cards")[0].children[i].children[0].children[0].children[1].style.backgroundColor = "springgreen";​
				}
				if(proyecto.estado == "Terminado"){
					document.getElementsByClassName("cards")[0].children[i].children[0].children[0].children[1].style.backgroundColor = "darkkhaki";
					
				}
				if(proyecto.estado == "Validado"){
					document.getElementsByClassName("cards")[0].children[i].children[0].children[0].children[1].style.backgroundColor = "skyblue";
					
					
				}
			}
			
			for (var j = 0; j < proyectos.length; j++) {
				var proyecto = proyectos[j];
				if (proyecto.Nombre === self.nombreProyecto()) {
					if(sessionStorage.userName != "admin"){
						document.getElementById('nombreProyecto').placeholder = proyecto.Nombre;
					}
					
					document.getElementById('nombrepro').innerText = proyecto.Nombre;

				}
			}
		}

		if(graficas != null){


			// Para la variable datos

			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				const tuplaHDD = {name: nombre_proyecto+"_HDD", value: graficas[i][0].HDD[2] }
				self.datos.push(tuplaHDD);
			}
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				const tuplaGrafica = {name: nombre_proyecto+"_Grafica", value: graficas[i][0].Grafica[2] }
				self.datos.push(tuplaGrafica);
			}
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				const tuplaMonitor = {name: nombre_proyecto+"_Monitor", value: graficas[i][0].Monitor[2] }
				self.datos.push(tuplaMonitor);
			}
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				const tuplaProcesador = {name: nombre_proyecto+"_Procesador", value: graficas[i][0].Procesador[2] }
				self.datos.push(tuplaProcesador);
			}
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				const tuplaDUT = {name: nombre_proyecto+"_DUT", value: graficas[i][0].DUT[2] }
				self.datos.push(tuplaDUT);
			}
			
			// // para la variable datos2
			
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				var lista = [];
				// self.datos2.push(nombre_proyecto);
				var tuplaHDD = {name:"HDD", value: graficas[i][0].HDD[2]}
				lista.push(tuplaHDD);
				var final = {name: nombre_proyecto, children: lista}
				self.datos2.push(final);
			}
			
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				var lista = [];
				// self.datos2.push(nombre_proyecto);
				var tuplaHDD = {name: "Grafica", value: graficas[i][0].Grafica[2]}
				lista.push(tuplaHDD);
				var final = {name: nombre_proyecto, children: lista}
				self.datos2.push(final);
			}
			
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				var lista = [];
				// self.datos2.push(nombre_proyecto);
				var tuplaHDD = {name:"Procesador", value: graficas[i][0].Procesador[2]}
				lista.push(tuplaHDD);
				var final = {name: nombre_proyecto, children: lista}
				self.datos2.push(final);
			}
			
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				var lista = [];
				// self.datos2.push(nombre_proyecto);
				var tuplaHDD = {name: "Monitor", value: graficas[i][0].Monitor[2]}
				lista.push(tuplaHDD);
				var final = {name: nombre_proyecto, children: lista}
				self.datos2.push(final);
			}
			
			for(var i = 0; i < graficas.length; i++){
				var parts = graficas[i][0].nombre.split("_");
				var nombre_proyecto = parts[0];
				var lista = [];
				// self.datos2.push(nombre_proyecto);
				var tuplaHDD = {name: "DUT", value: graficas[i][0].DUT[2]}
				lista.push(tuplaHDD);
				var final = {name: nombre_proyecto, children: lista}
				self.datos2.push(final);
			}
			
			
			
		// // para la variable datos4
			var e = {
				    "name": "Dummy",
				    "disabled": true,
				    "value": 1000,
				    "color": am4core.color("#dadada"),
				    "opacity": 0.3,
				    "strokeDasharray": "4,4"
				};
			self.datos4.push(e);
			for(var i = 0; i < self.datos.length; i++){
				self.datos4.push(self.datos[i]);
			}
			
			
			var nombre_proyecto = graficas;
			var chart = document.getElementById("chartdiv");
			var chart2 = document.getElementById("chartdiv2");
			var chart3 = document.getElementById("chartdiv3");
			var cha = document.getElementById("cha");
			var boxes = document.getElementById("boxes");
			boxes.style.display = "none";
			chart.style.display = "flex";
			chart2.style.display = "flex";
			chart3.style.display = "flex";
			cha.style.display = "flex";
			
			// var boton = document.getElementsByClassName("elegirProyecto");
			// boton[0].style.display = "flex";
			
			am4core.ready;
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
