import React, { useEffect, useState } from 'react';
import axios from 'axios';

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    axios.get('http://localhost:8080/api/user', { withCredentials: true })
      .then(response => setUser(response.data))
      .catch(() => setUser(null));
  }, []);

  const loginUrl = 'http://localhost:8080/oauth2/authorization/google';
  const logoutUrl = 'http://localhost:8080/logout';

  return (
    <div style={{ textAlign: 'center', marginTop: '3rem' }}>
      <h1>Spring OAuth2 Login Demo</h1>
      {user ? (
        <>
          <img src={user.picture} alt="avatar" width={100} style={{ borderRadius: 50 }} />
          <h2>{user.name}</h2>
          <p>{user.email}</p>
          <a href={logoutUrl}><button>Logout</button></a>
        </>
      ) : (
        <a href={loginUrl}><button>Login with Google</button></a>
      )}
    </div>
  );
}

export default App;
