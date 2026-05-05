import Footer from '../shared/Footer';
import NavBar from '../shared/NavBar';
import SysAdminBody from '../shared/SysAdminBody';

export default function SysAdminView() {
    return (
        <div className="sysAdminView">
            <NavBar />
            <SysAdminBody />
            <Footer />
        </div>
    )
}