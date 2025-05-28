import { BrowserRouter as Router, Route, Routes  } from 'react-router-dom'
import Sign from './pages/sign'
import Login from './pages/login'
import UserListUI from './pages/UserList'
import './App.css'

function App() {
  return (
    <>
      <Router>
       <div className="App">
        <Routes>
          <Route path="/sign" element={<Sign/>} />
        </Routes>
        <Routes>
          <Route path="/login" element={<Login/>} />
        </Routes>
        <Routes>
          <Route path='/users' element={<UserListUI/>}/>
        </Routes>
      </div>
    </Router>
    </>
  )
}

export default App
