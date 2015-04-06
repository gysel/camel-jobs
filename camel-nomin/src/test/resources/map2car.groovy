import org.apache.camel.component.Car
import org.apache.camel.component.Car.EngineType

mappingFor a: java.util.Map, b: Car

b.nrOfWheels = 4
b.brand = a['MARKE']
b.model = a['MODELL']
b.engineType = a['MOTORENTYP']
// first value is from side a and b is the second value.
simple (["Diesel",EngineType.DIESEL], ["Benzin",EngineType.PETROL], ["Elektrisch",EngineType.ELECTRIC])
