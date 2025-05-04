document.addEventListener("DOMContentLoaded", () => {
    const chatMessages = document.getElementById("chat-messages")
    const userInput = document.getElementById("user-input")
    const sendButton = document.getElementById("send-button")
    const typingIndicator = document.getElementById("typing-indicator")
    const recommendedDrinks = document.getElementById("recommended-drinks")
    const drinkList = document.getElementById("drink-list")
    const sendSpinner = document.getElementById("send-spinner")

    // Focus on input when page loads
    setTimeout(() => {
        userInput.focus()
    }, 1000)

    // Function to add a message to the chat with typing animation
    function addMessage(message, isUser) {
        const messageDiv = document.createElement("div")
        messageDiv.classList.add("message")
        messageDiv.classList.add(isUser ? "user-message" : "bot-message")

        // Insert before typing indicator
        chatMessages.insertBefore(messageDiv, typingIndicator)

        if (isUser) {
            // User messages appear immediately
            messageDiv.textContent = message
            messageDiv.classList.add("animate__animated", "animate__fadeInRight")
        } else {
            // For bot messages, we'll use a different approach to ensure text doesn't get cut off
            messageDiv.innerHTML = message // Use innerHTML instead of textContent
            messageDiv.classList.add("animate__animated", "animate__fadeIn")
        }

        // Scroll to bottom
        chatMessages.scrollTop = chatMessages.scrollHeight
    }

    // Function to show typing indicator
    function showTypingIndicator() {
        typingIndicator.style.display = "block"
        typingIndicator.classList.add("animate__animated", "animate__fadeIn")
        chatMessages.scrollTop = chatMessages.scrollHeight

        // Show loading spinner on send button
        sendButton.querySelector("i").style.display = "none"
        sendSpinner.style.display = "block"

        // Disable input and button while processing
        userInput.disabled = true
        sendButton.disabled = true
    }

    // Function to hide typing indicator
    function hideTypingIndicator() {
        typingIndicator.classList.add("animate__animated", "animate__fadeOut")

        setTimeout(() => {
            typingIndicator.style.display = "none"
            typingIndicator.classList.remove("animate__animated", "animate__fadeOut")
        }, 300)

        // Hide loading spinner on send button
        sendButton.querySelector("i").style.display = "block"
        sendSpinner.style.display = "none"

        // Re-enable input and button
        userInput.disabled = false
        sendButton.disabled = false
        userInput.focus()
    }

    // Function to display recommended drinks with animation
    function displayRecommendedDrinks(drinks) {
        if (drinks && drinks.length > 0) {
            // Clear previous recommendations
            drinkList.innerHTML = ""

            // Add each drink to the list with staggered animation
            drinks.forEach((drink, index) => {
                const drinkCard = document.createElement("div")
                drinkCard.classList.add("drink-card")
                drinkCard.style.animationDelay = `${index * 0.1}s`

                const drinkLink = document.createElement("a")
                drinkLink.href = `/shop/product/${drink.id}`

                const drinkImage = document.createElement("img")
                drinkImage.src = drink.coverImage
                drinkImage.alt = drink.title

                const drinkTitle = document.createElement("h4")
                drinkTitle.textContent = drink.title

                const drinkPrice = document.createElement("p")
                drinkPrice.textContent = formatPrice(drink.salePrice) + " VND"

                drinkLink.appendChild(drinkImage)
                drinkCard.appendChild(drinkLink)
                drinkCard.appendChild(drinkTitle)
                drinkCard.appendChild(drinkPrice)

                drinkList.appendChild(drinkCard)
            })

            // Show the recommendations section with animation
            recommendedDrinks.style.display = "block"
            recommendedDrinks.classList.add("animate__animated", "animate__fadeInUp")

            // Remove animation classes after animation completes
            setTimeout(() => {
                recommendedDrinks.classList.remove("animate__animated", "animate__fadeInUp")
            }, 1000)
        } else {
            // Hide the recommendations section if no drinks
            recommendedDrinks.classList.add("animate__animated", "animate__fadeOutDown")

            setTimeout(() => {
                recommendedDrinks.style.display = "none"
                recommendedDrinks.classList.remove("animate__animated", "animate__fadeOutDown")
            }, 300)
        }
    }

    // Function to format price
    function formatPrice(price) {
        if (!price) return "0"
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")
    }

    // Function to send message to server
    function sendMessage() {
        const message = userInput.value.trim()

        if (message) {
            // Add user message to chat
            addMessage(message, true)

            // Clear input
            userInput.value = ""

            // Show typing indicator
            showTypingIndicator()

            // Send message to server
            fetch("/chat/send", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ message: message }),
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Network response was not ok")
                    }
                    return response.json()
                })
                .then((data) => {
                    // Hide typing indicator after a minimum delay for better UX
                    setTimeout(
                        () => {
                            hideTypingIndicator()

                            // Add bot response to chat
                            addMessage(data.message, false)

                            // Display recommended drinks after a short delay
                            setTimeout(() => {
                                displayRecommendedDrinks(data.recommendedDrinks)
                            }, 1000)
                        },
                        Math.max(1000, data.message.length * 20),
                    ) // Minimum 1 second, or longer for longer messages
                })
                .catch((error) => {
                    console.error("Error:", error)

                    // Hide typing indicator
                    setTimeout(() => {
                        hideTypingIndicator()

                        // Add error message
                        addMessage("Xin lỗi, đã xảy ra lỗi khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.", false)
                    }, 1000)
                })
        }
    }

    // Event listeners
    sendButton.addEventListener("click", sendMessage)

    userInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            sendMessage()
        }
    })

    // Add animation to initial message
    setTimeout(() => {
        const initialMessage = chatMessages.querySelector(".bot-message")
        if (initialMessage) {
            initialMessage.classList.add("animate__animated", "animate__fadeIn")
        }
    }, 500)
})
