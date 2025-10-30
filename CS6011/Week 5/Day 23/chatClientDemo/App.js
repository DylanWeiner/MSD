import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import {useState} from "react";

export default function App() {
    const [message, setMessage] = useState(''); //setMessage will change the file and trigger a rerender.
    //If you setMessage, it marks up the value for rerendering, so it
    // will continue execution and then rerender once all the code has run.

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
