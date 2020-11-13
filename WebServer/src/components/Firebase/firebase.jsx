import app from 'firebase/app';
import 'firebase/auth';

const config = {
    apiKey: "AIzaSyBzOjfhyWdn-cZC3yDku-Q_4dwaqKHrHyU",
    authDomain: "bodilysensors-2020.firebaseapp.com",
    databaseURL: "https://bodilysensors-2020.firebaseio.com",
    projectId: "bodilysensors-2020",
    storageBucket: "bodilysensors-2020.appspot.com",
    messagingSenderId: "487006130990",
};

class Firebase {
    constructor() {
        app.initializeApp(config);

        this.auth = app.auth();
    }

    doCreateUserWithEmailAndPassword = (email, password) =>
        this.auth.createUserWithEmailAndPassword(email,password);

    doSignInWithEmailAndPassword = (email, password) =>
    this.auth.signInWithEmailAndPassword(email, password);
        
    doSignOut = () => this.auth.signOut();

    doPasswordReset = email => this.auth.sendPasswordResetEmail(email);

    doPasswordUpdate = password => this.auth.currentUser.updatePassword(password);
}

export default Firebase;