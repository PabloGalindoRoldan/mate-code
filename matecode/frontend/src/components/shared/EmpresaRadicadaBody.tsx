import "./EmpresaRadicadaBody.css";
import InfoPanel from "./InfoPanel";
import StatisticsPanel from "./StatisticsPanel";


export default function EmpresaRadicadaBody(params: { empresaInfo: any }) {

    return (
        <div className="empresaRadicadaBody">
            <InfoPanel empresaInfo={params.empresaInfo} />
            <StatisticsPanel empresaInfo={params.empresaInfo} />
        </div>
    );
}