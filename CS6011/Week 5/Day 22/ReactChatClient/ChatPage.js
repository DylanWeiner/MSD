import { useRef, useEffect } from "react";
import {ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View} from "react-native";
import React from "react";

function ChatPage({chatRoomName, allMessages, message }){

    const messageRef = useRef('');
    const usernameRef = useRef('');
    const roomRef = useRef('');
    const scrollViewRef = useRef(null);

    useEffect(() => {
        scrollViewRef.current?.scrollToEnd({animated:true});
        console.log("This is working");
    }, [allMessages]);

    return (
        <View style={styles.container}>
            <Text style={styles.chatRoomName}>{chatRoomName}</Text>

            <TouchableOpacity
                style={styles.leaveButton}
                onPress={() => message({type: "leave"}, usernameRef.current, roomRef.current, "leave")}
            >
                <Text>Leave</Text>
            </TouchableOpacity>

            <ScrollView
                style={styles.messageBox}
                ref={scrollViewRef}
                onContentSizeChange={() => scrollViewRef.current?.scrollToEnd({ animated: true })}
            >
                {allMessages.slice(-40).map((msg, index) => (
                    <Text key={index} style={{ color: msg.color || "black", marginVertical: 2 }}>
                        {msg.text}
                    </Text>
                ))}
            </ScrollView>

            <TextInput
                style={styles.messageInput}
                onChangeText={text => (messageRef.current = text)}
                onSubmitEditing={() => message({type: "message"}, usernameRef.current, roomRef.current, messageRef.current)}
                placeholder="Type your message"
                returnKeyType="send"
            />

            <TouchableOpacity
                style={styles.sendButton}
                onPress={() => message({type: "message"}, usernameRef.current, roomRef.current, messageRef.current)}
            >
                <Text>Send</Text>
            </TouchableOpacity>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 10,
        backgroundColor: "#C9F5D9",
    },
    chatRoomName: {
        fontSize: 20,
        fontWeight: "bold",
        marginBottom: 10,
        marginVertical:50,
    },
    leaveButton: {
        position: "absolute",
        top: 16,
        right: 20,
        backgroundColor: "#C9F5D9",
        borderColor: "#3CD184",
        borderWidth: 1,
        paddingHorizontal: 10,
        paddingVertical: 6,
        borderRadius: 4,
        zIndex: 1,
        marginVertical:50,
    },
    messageBox: {
        flex: 1,
        marginTop: 50,
        marginBottom: 120, // space for input and send button
        backgroundColor: "#F0FFF4",
        borderRadius: 5,
        padding: 10,
    },
    messageInput: {
        position: "absolute",
        bottom: 60,
        left: 10,
        right: 110,
        height: 40,
        backgroundColor: "#C9F5D9",
        borderColor: "#3CD184",
        borderWidth: 1,
        paddingHorizontal: 10,
        borderRadius: 5,
    },
    sendButton: {
        position: "absolute",
        bottom: 55,
        right: 20,
        width: 80,
        height: 46,
        backgroundColor: "#C9F5D9",
        borderColor: "#3CD184",
        borderWidth: 1,
        borderRadius: 5,
        justifyContent: "center",
        alignItems: "center",
    },
});

export default ChatPage;