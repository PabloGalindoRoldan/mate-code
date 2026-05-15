import { Navigate } from 'react-router';
import { useAuth } from '../../context/AuthContext';
import { type ReactNode } from 'react';

interface ProtectedRouteProps {
    children: ReactNode;
    allowedRoles?: string[];
}

export default function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
    const { user, loading } = useAuth();

    // Wait for the context to finish reading from sessionStorage
    if (loading) {
        return <div className="loading-spinner">Cargando...</div>;
    }

    // 1. If not authenticated, redirect straight to login
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    // 2. If authenticated but the user's role is not explicitly authorized for this route
    if (allowedRoles && !allowedRoles.includes(user.rol)) {
        // You can redirect to an unauthorized page, or back to their role's home view
        return <Navigate to="/" replace />;
    }

    // 3. Authorized! Render the requested component view
    return children;
}