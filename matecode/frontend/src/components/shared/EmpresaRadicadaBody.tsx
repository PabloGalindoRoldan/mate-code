import "./EmpresaRadicadaBody.css";
import InfoPanel from "./InfoPanel";
import StatisticsPanel from "./StatisticsPanel";


export default function EmpresaRadicadaBody(params: { empresaInfo: any, isMenuOpen: boolean }) {
    const { isMenuOpen } = params;
    const { empresaInfo } = params;

    return (
        <div className={`empresaRadicadaBody ${isMenuOpen ? "shrunk" : "full"}`}>
            <InfoPanel empresaInfo={empresaInfo} />
            <StatisticsPanel empresaInfo={empresaInfo} />
        </div>
    );
}