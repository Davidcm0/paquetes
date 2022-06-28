package com.rfranco.paquetes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import model.Pedido;
import model.Vehiculo;
import persistencia.PaqueteDAO;
import persistencia.VehiculoDAO;

@SpringBootTest
class PaquetesApplicationTests {

	VehiculoDAO vDAO;
	PaqueteDAO pDAO;
	
	@Test
	public void TestCreateVehiculo() {
		Vehiculo v = new Vehiculo("matricula","marca","modelo","color","pass");
		vDAO.insertar(v);
		assert (vDAO.leer_vehiculo(v.getMatricula())) != null;
	}
	
	@Test
	public void TestCreatePedido() {
		ArrayList<String> ubicaciones = null;
		Pedido p = new Pedido(12345,ubicaciones, "4567SRT");
		pDAO.insertar(p);
		assert (pDAO.leer_pedido(p.getId())) != null;
	}
	
	@Test
	public void testReadAllVehiculo() {
		List<Vehiculo> vehiculos = vDAO.leer_vehiculos();
		assertThat(vehiculos).size().isGreaterThan(0);
	}
	
	@Test
	public void testReadAllPedidos() {
		List<Pedido> pedidos = pDAO.leer_pedidos();
		assertThat(pedidos).size().isGreaterThan(0);
	}
	
	@Test
	public void testSingleVehiculo() {
		Vehiculo v = vDAO.leer_vehiculo("4567STR");
		assertEquals("audi",v.getMarca());
	}
	
	@Test
	public void testSinglePedido() {
		Pedido p = pDAO.leer_pedido(12345);
		assertEquals("4567SRT",p.getVehiculo());
	}
	
	@Test
	public void testUpdate() {
		Vehiculo v = vDAO.leer_vehiculo("4567STR");
		String marcaVieja = v.getMarca();
		vDAO.actualizar_vehiculo("4567STR", "", "marcanueva", "modelo", "color");
		v = vDAO.leer_vehiculo("4567STR");
		assertNotEquals(marcaVieja,v.getMarca());
	}
	
	@Test
	public void testUpdateUbicacion() {
		Pedido p = pDAO.leer_pedido(12345);
		ArrayList<String> ubicaciones = p.getUbicaciones();
		String ultimaUbi = ubicaciones.get(ubicaciones.size()-1);
		pDAO.actualizar_ubicacion(12345, "calle 13");
		p = pDAO.leer_pedido(12345);
	    ubicaciones = p.getUbicaciones();
		String nuevaUbi = ubicaciones.get(ubicaciones.size()-1); 
		assertNotEquals(ultimaUbi,nuevaUbi);
	}
	
	@Test
	public void testDelete() {
		pDAO.eliminar_pedido(123456);
		assert (pDAO.leer_pedido(12345)) == null;
	}
		
}
