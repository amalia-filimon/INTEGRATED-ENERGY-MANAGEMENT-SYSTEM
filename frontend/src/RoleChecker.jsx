// src/RoleChecker.js
import React from 'react';

//Aceasta functie verifica dacă rolul utilizatorului curent (obtinut din localStorage) se afla în lista de roluri permise (roles).
// Daca rolul utilizatorului este permis, componenta RoleChecker va returna componentele copil, altfel va redirectiona utilizatorul catre pagina de login.
function RoleChecker({ roles, children }) {
    const userRole = localStorage.getItem('userRole');

    if (!roles.includes(userRole)) {
        window.location.href = '/login';
        return null;
    }

    return children;
}

export default RoleChecker;
