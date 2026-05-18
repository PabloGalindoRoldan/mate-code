import Footer from '../../ui/footer/Footer';
import NavBar from '../../ui/navBar/NavBar';
import SysAdminBody from './SysAdminBody';

export default function SysAdminView() {
    return (
        <div className="sysAdminView">
            <NavBar />
            <SysAdminBody />
            <Footer />
        </div>
    )
}