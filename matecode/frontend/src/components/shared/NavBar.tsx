
import { useNavigate } from "react-router";
import './NavBar.css'


export default function NavBar() {
  const navigate = useNavigate();

  return (
    <>
      <nav className="navbar">
        <h1 className="logo"><a onClick={() => navigate('/')}>RN</a></h1>
        <ul className="buttonList">
          <li><button className="navButtonContacto" onClick={() => navigate('/')}>Parque</button></li>
          <li><button className="navButtonContacto" onClick={() => navigate('/admin')}>Admin</button></li>
          <li><button className="navButtonContacto" onClick={() => navigate('/nueva-empresa')}>Nueva Empresa</button></li>
          <li><button className="navButtonContacto" onClick={() => navigate('/empresa-radicada')}>Empresa Radicada</button></li>
          <li><button className="navButtonContacto" onClick={() => navigate('/contacto')}>Contacto</button></li>
          <li><button className="navButtonIngresar" onClick={() => navigate('/login')}>Ingresar</button></li>
          <li><button className="navButtonRegistrarse" onClick={() => navigate('/register')}>Registrarse</button></li>
        </ul>
      </nav>
    </>
  )
}