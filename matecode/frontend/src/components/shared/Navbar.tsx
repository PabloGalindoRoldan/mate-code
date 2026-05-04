
import './NavBar.css'

export default function Navbar() {
  return (
    <>
      <nav className="navbar">
        <h1 className="logo">RN</h1>
        <ul className="buttonList">
          <li><button className="navButtonContacto">Contacto</button></li>
          <li><button className="navButtonIngresar">Ingresar</button></li>
          <li><button className="navButtonRegistrarse">Registrarse</button></li>
        </ul>
      </nav>
    </>
  )
}