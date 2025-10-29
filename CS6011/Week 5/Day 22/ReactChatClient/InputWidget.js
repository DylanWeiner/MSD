import React, { useRef } from "react";
import {TextInput, View, Text, StyleSheet, TouchableOpacity} from "react-native";

function InputWidget({login}) {
    const usernameValueRef = useRef('');
    const roomValueRef = useRef('');

    const usernameInputRef = useRef(null);
    const roomInputRef = useRef(null);

    return (
        <View style={{marginVertical:50}}>
            <Text style={{textAlign: 'center' }}>Chatroom</Text>

            <View style={{ marginVertical: 15 }}>
                <Text>Username</Text>
                <TextInput
                    style={styles.userName}
                    onChangeText={text => (usernameValueRef.current = text)}
                    onSubmitEditing={() => roomInputRef.current?.focus()}
                    returnKeyType="next"
                    autoCorrect={false}
                    autoCapitalize="none"
                    ref={usernameInputRef}
                />
            </View>

            <View style={{ marginVertical: 35 }}>
                <Text>Room</Text>
                <TextInput
                    style={styles.room}
                    onChangeText={text => (roomValueRef.current = text)}
                    onSubmitEditing={(e) => {login(e, usernameValueRef.current, roomValueRef.current)}}
                    returnKeyType="done"
                    autoCorrect={false}
                    autoCapitalize="none"
                    ref={roomInputRef}
                />
            </View>

            <TouchableOpacity
                onPress={(e) => {login(e, usernameValueRef.current, roomValueRef.current)}}
            >
                <Text style={styles.joinButton}>Enter</Text>
            </TouchableOpacity>
        </View>
    );
}

const styles = StyleSheet.create({
    joinButton: {
        padding: 15,
        backgroundColor: '#C9F5D9',
        borderColor: '#3CD184',
        borderWidth: 1,
        textAlign: "center",
    },
    userName: {
        textAlign: "center",
        backgroundColor: '#C9F5D9',
        borderColor: '#3CD184',
        borderWidth: 1,
    },
    room: {
        textAlign: "center",
        backgroundColor: '#C9F5D9',
        borderColor: '#3CD184',
        borderWidth: 1,
    },
});

export default InputWidget;