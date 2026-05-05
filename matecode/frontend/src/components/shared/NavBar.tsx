
import { useNavigate } from "react-router";
import './NavBar.css'


export default function NavBar() {
  const navigate = useNavigate();

  return (
    <>
      <nav className="navbar">
        <h1 className="logo"><a onClick={() => navigate('/')}>RN</a></h1>
        <ul className="buttonList">
          <li><button className="navButtonContacto" onClick={() => navigate('/contact')}>Contacto</button></li>
          <li><button className="navButtonIngresar" onClick={() => navigate('/login')}>Ingresar</button></li>
          <li><button className="navButtonRegistrarse" onClick={() => navigate('/register')}>Registrarse</button></li>
        </ul>
      </nav>
    </>
  )
}