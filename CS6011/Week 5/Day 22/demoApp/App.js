import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import {useEffect, useRef} from "react";

export default function App() {

    let ws = useRef(null);
    useEffect(() => {


    ws.current = new WebSocket('ws://127.0.0.1:8080');
    //For Android exclusively, it is ws://10.0.2.2:8080

    ws.current.onopen = () => {ws.current.send("join Ahmad Games");}
    ws.current.onmessage = (e) => {console.log(e);}
    ws.current.onclose = (e) => {console.log(e);}
    ws.current.onerror = (e) => {console.log(e);}

    }, []);

    useEffect(() => {

    }, []);

  return (
    <View style={styles.container}>
      <Text>Open up App.js to start working on your app!</Text>
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
