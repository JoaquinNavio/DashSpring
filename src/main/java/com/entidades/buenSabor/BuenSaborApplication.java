package com.entidades.buenSabor;

import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.domain.enums.*;
import com.entidades.buenSabor.repositories.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.Set;


@SpringBootApplication
public class BuenSaborApplication {
	private static final Logger logger = LoggerFactory.getLogger(BuenSaborApplication.class);

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EmpleadoRepository empleadoRepository;
	@Autowired
	private ImagenClienteRepository imagenClienteRepository;
	@Autowired
	private ImagenEmpleadoRepository imagenEmpleadoRepository;
	@Autowired
	private PromocionDetalleRepository promocionDetalleRepository;

	@Autowired
	private UsuarioClienteRepository usuarioClienteRepository;
	@Autowired
	private UsuarioEmpleadoRepository usuarioEmpleadoRepository;

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private ProvinciaRepository provinciaRepository;

	@Autowired
	private LocalidadRepository localidadRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private SucursalRepository sucursalRepository;

	@Autowired
	private DomicilioRepository domicilioRepository;

	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ArticuloInsumoRepository articuloInsumoRepository;

	@Autowired
	private ArticuloManufacturadoRepository articuloManufacturadoRepository;

	@Autowired
	private ImagenArticuloRepository imagenArticuloRepository;
	@Autowired
	private ImagenPromocionRepository imagenPromocionRepository;

	@Autowired
	private PromocionRepository promocionRepository;

	@Autowired
	private ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(BuenSaborApplication.class, args);
		logger.info("Estoy activo en el main");
	}

	@Bean
	@Transactional
	CommandLineRunner init(ClienteRepository clienteRepository,
						   PromocionDetalleRepository promocionDetalleRepository,
						   PaisRepository paisRepository,
						   ProvinciaRepository provinciaRepository,
						   LocalidadRepository localidadRepository,
						   EmpresaRepository empresaRepository,
						   SucursalRepository sucursalRepository,
						   DomicilioRepository domicilioRepository,
						   UnidadMedidaRepository unidadMedidaRepository,
						   CategoriaRepository categoriaRepository,
						   ArticuloInsumoRepository articuloInsumoRepository,
						   ArticuloManufacturadoRepository articuloManufacturadoRepository,
						   ImagenArticuloRepository imagenArticuloRepository,
						   PromocionRepository promocionRepository,
						   PedidoRepository pedidoRepository,
						   EmpleadoRepository empleadoRepository, FacturaRepository facturaRepository) {
		return args -> {
			logger.info("----------------ESTOY----FUNCIONANDO---------------------");
			// Etapa del dashboard
			// Crear 1 pais
			// Crear 2 provincias para ese pais
			// crear 2 localidades para cada provincia
			Pais pais1 = Pais.builder().nombre("Argentina").build();
			paisRepository.save(pais1);
			//CREACION DE PROVINCIAS
			Provincia provincia1 = Provincia.builder().nombre("Mendoza").pais(pais1).build();
			Provincia provincia2 = Provincia.builder().nombre("Buenos Aires").pais(pais1).build();
			provinciaRepository.save(provincia1);
			provinciaRepository.save(provincia2);

			//CREACION DE LOCALIDADES
			Localidad localidad1 = Localidad.builder().nombre("Lujan de Cuyo").provincia(provincia1).build();
			Localidad localidad2 = Localidad.builder().nombre("Guaymallen").provincia(provincia1).build();
			Localidad localidad3 = Localidad.builder().nombre("Mar del Plata").provincia(provincia2).build();
			Localidad localidad4 = Localidad.builder().nombre("Mar de las Pampas").provincia(provincia2).build();

			localidadRepository.save(localidad1);
			localidadRepository.save(localidad2);
			localidadRepository.save(localidad3);
			localidadRepository.save(localidad4);

			// Crear 1 empresa, 2 sucursales para esa empresa y los Domicilios para esas sucursales

			Empresa empresaCarlos = Empresa.builder().nombre("Lo de Carlos").cuil(30546780L).razonSocial("Venta de Alimentos").build();
			empresaRepository.save(empresaCarlos);

			Sucursal sucursalGuaymallen = Sucursal.builder().
					nombre("En Guaymallen").horarioApertura(LocalTime.of(17,0)).
					horarioCierre(LocalTime.of(23,0)).
					build();

			Sucursal sucursalMarDelPlata = Sucursal.builder().nombre("En MDQ").
					horarioApertura(LocalTime.of(16,0)).
					horarioCierre(LocalTime.of(23,30)).build();

			Domicilio domicilioBerutti = Domicilio.builder().cp(5519).calle("Berutti").numero(2684).piso(0).nroDpto(5).
					localidad(localidad1).build();

			Domicilio domicilioGaboto = Domicilio.builder().cp(7600).calle("Gaboto").numero(3475).
					localidad(localidad2).build();
			// GRABAMOS DOMICILIOS
			domicilioRepository.save(domicilioBerutti);
			domicilioRepository.save(domicilioGaboto);

			//ASOCIAMOS LOS DOMICILIOS A SUCURSAL
			sucursalGuaymallen.setDomicilio(domicilioBerutti);
			sucursalMarDelPlata.setDomicilio(domicilioGaboto);

			//ASOCIAMOS SUCURSALES A EMPRESA
			empresaCarlos.getSucursales().add(sucursalGuaymallen);
			empresaCarlos.getSucursales().add(sucursalMarDelPlata);

			//ASIGNAMOS EMPRESA A SUCURSALES
			sucursalGuaymallen.setEmpresa(empresaCarlos);
			sucursalMarDelPlata.setEmpresa(empresaCarlos);
			// Grabo las sucursales
			sucursalRepository.save(sucursalGuaymallen);
			sucursalRepository.save(sucursalMarDelPlata);
			// Grabi empresa
			empresaRepository.save(empresaCarlos);

			// Crear Categorías de productos y subCategorías de los mismos
			Categoria categoriaBebidas = Categoria.builder().denominacion("Bebidas").
					build();
			categoriaRepository.save(categoriaBebidas);

			Categoria categoriaGaseosas = Categoria.builder().denominacion("Gaseosas").
					build();
			categoriaRepository.save(categoriaGaseosas);

			Categoria categoriaTragos = Categoria.builder().denominacion("Tragos").
					build();
			categoriaRepository.save(categoriaTragos);

			Categoria categoriaPizzas = Categoria.builder().denominacion("Pizzas").
					build();



			Categoria categoriaInsumos = Categoria.builder().denominacion("Insumos").
					build();

			// Grabo la categoría de insumos y de Manufacturados
			categoriaRepository.save(categoriaPizzas);
			categoriaRepository.save(categoriaInsumos);
			// Asigno subCategorías

			categoriaBebidas.getSubCategorias().add(categoriaGaseosas);
			categoriaBebidas.getSubCategorias().add(categoriaTragos);
			// Grabo las Subcategorías
			categoriaRepository.save(categoriaBebidas);

			logger.info("---------------voy a asignar a Guaymallen--------------------");
			//ASOCIAMOS CATEGORIAS CON SUCURSAL
			categoriaInsumos.getSucursales().add(sucursalGuaymallen);
			// Cargo las categorias a la sucursal guaymallen
			sucursalGuaymallen.getCategorias().add(categoriaInsumos);
			sucursalGuaymallen.getCategorias().add(categoriaBebidas);
			sucursalGuaymallen.getCategorias().add(categoriaGaseosas);
			sucursalGuaymallen.getCategorias().add(categoriaTragos);
			sucursalGuaymallen.getCategorias().add(categoriaPizzas);
			logger.info("{}",sucursalGuaymallen);
			// Grabo las categorias que vende esa sucursal
			sucursalRepository.save(sucursalGuaymallen);

			logger.info("---------------saque el save de abajo-------------------");


			logger.info("---------------grabe guaymallen--------------------");

			logger.info("---------------voy a asignar a Mardel Plata--------------------");
			categoriaInsumos.getSucursales().add(sucursalMarDelPlata);
			// Cargo las categorias a la sucursal Mardel Plata
			sucursalMarDelPlata.getCategorias().add(categoriaInsumos);
			sucursalMarDelPlata.getCategorias().add(categoriaBebidas);
			sucursalMarDelPlata.getCategorias().add(categoriaGaseosas);
			sucursalMarDelPlata.getCategorias().add(categoriaTragos);
			sucursalMarDelPlata.getCategorias().add(categoriaPizzas);
// Grabo las categorias que vende esa sucursal
			sucursalRepository.save(sucursalMarDelPlata);

			logger.info("---------------grabe Mardel Plata--------------------");

		};
	}

}