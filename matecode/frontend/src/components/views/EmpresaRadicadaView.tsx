import EmpresaRadicadaBody from "../shared/EmpresaRadicadaBody";
import Footer from "../shared/Footer";
import NavBar from "../shared/NavBar";
import "./EmpresaRadicadaView.css";
import data from "../../../tmp/empresaInfo.json";
import { useState } from "react";


export default function EmpresaRadicadaView() {

    const [empresaInfo] = useState(data);

    return (
        <div className="empresaRadicadaView">
            <NavBar />
            <EmpresaRadicadaBody empresaInfo={empresaInfo} />
            <Footer />
        </div>
    );
}

