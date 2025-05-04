document.addEventListener("DOMContentLoaded", () => {
    console.log("Chat widget script loaded")

    // Check if chat widget elements exist
    const chatWidget = document.getElementById("chat-widget")
    const chatToggle = document.getElementById("chat-widget-toggle")
    const closeChat = document.getElementById("close-chat")
    const widgetMessages = document.getElementById("widget-messages")
    const widgetUserInput = document.getElementById("widget-user-input")
    const widgetSendButton = document.getElementById("widget-send-button")
    const widgetTypingIndicator = document.getElementById("widget-typing-indicator")
    const widgetRecommendedDrinks = document.getElementById("widget-recommended-drinks")
    const widgetDrinkList = document.getElementById("widget-drink-list")
    const widgetSendSpinner = document.getElementById("widget-send-spinner")

    if (chatWidget && chatToggle) {
        console.log("Chat widget elements found")

        // Toggle chat widget
        chatToggle.addEventListener("click", (e) => {
            e.preventDefault()
            console.log("Chat toggle clicked")
            if (chatWidget.style.display === "flex") {
                closeChat.click()
            } else {
                chatWidget.style.display = "flex"
                chatWidget.classList.add("animate__animated", "animate__fadeIn")
                chatToggle.classList.remove("animate__pulse", "animate__infinite", "pulse-notification")

                // Focus on input after opening
                setTimeout(() => {
                    widgetUserInput.focus()
                }, 300)
            }
        })

        // Close chat widget
        if (closeChat) {
            closeChat.addEventListener("click", () => {
                chatWidget.classList.add("animate__animated", "animate__fadeOut")

                setTimeout(() => {
                    chatWidget.style.display = "none"
                    chatWidget.classList.remove("animate__animated", "animate__fadeOut")
                }, 300)
            })
        }

        // Function to add a message to the chat with typing animation
        function addWidgetMessage(message, isUser) {
            if (!widgetMessages) return

            const messageDiv = document.createElement("div")
            messageDiv.classList.add("message")
            messageDiv.classList.add(isUser ? "user-message" : "bot-message")

            // Insert before typing indicator
            if (widgetTypingIndicator) {
                widgetMessages.insertBefore(messageDiv, widgetTypingIndicator)
            } else {
                widgetMessages.appendChild(messageDiv)
            }

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
            widgetMessages.scrollTop = widgetMessages.scrollHeight
        }

        // Function to show typing indicator
        function showWidgetTypingIndicator() {
            if (widgetTypingIndicator) {
                widgetTypingIndicator.style.display = "block"
                widgetTypingIndicator.classList.add("animate__animated", "animate__fadeIn")
                widgetMessages.scrollTop = widgetMessages.scrollHeight
            }

            // Show loading spinner on send button
            if (widgetSendButton && widgetSendSpinner) {
                const icon = widgetSendButton.querySelector("i")
                if (icon) icon.style.display = "none"
                widgetSendSpinner.style.display = "block"
            }

            // Disable input and button while processing
            if (widgetUserInput) widgetUserInput.disabled = true
            if (widgetSendButton) widgetSendButton.disabled = true
        }

        // Function to hide typing indicator
        function hideWidgetTypingIndicator() {
            if (widgetTypingIndicator) {
                widgetTypingIndicator.classList.add("animate__animated", "animate__fadeOut")

                setTimeout(() => {
                    widgetTypingIndicator.style.display = "none"
                    widgetTypingIndicator.classList.remove("animate__animated", "animate__fadeOut")
                }, 300)
            }

            // Hide loading spinner on send button
            if (widgetSendButton && widgetSendSpinner) {
                const icon = widgetSendButton.querySelector("i")
                if (icon) icon.style.display = "block"
                widgetSendSpinner.style.display = "none"
            }

            // Re-enable input and button
            if (widgetUserInput) {
                widgetUserInput.disabled = false
                widgetUserInput.focus()
            }
            if (widgetSendButton) widgetSendButton.disabled = false
        }

        // Function to display recommended drinks with animation
        function displayWidgetRecommendedDrinks(drinks) {
            if (!widgetRecommendedDrinks || !widgetDrinkList) return

            if (drinks && drinks.length > 0) {
                // Clear previous recommendations
                widgetDrinkList.innerHTML = ""

                // Add each drink to the list with staggered animation
                drinks.forEach((drink, index) => {
                    const drinkCard = document.createElement("div")
                    drinkCard.classList.add("widget-drink-card")
                    drinkCard.style.animationDelay = `${index * 0.1}s`

                    const drinkLink = document.createElement("a")
                    drinkLink.href = `/shop/product/${drink.id}`

                    const drinkImage = document.createElement("img")
                    drinkImage.src = drink.coverImage
                    drinkImage.alt = drink.title

                    const drinkTitle = document.createElement("h5")
                    drinkTitle.textContent = drink.title

                    const drinkPrice = document.createElement("p")
                    drinkPrice.textContent = formatWidgetPrice(drink.salePrice) + " VND"

                    drinkLink.appendChild(drinkImage)
                    drinkCard.appendChild(drinkLink)
                    drinkCard.appendChild(drinkTitle)
                    drinkCard.appendChild(drinkPrice)

                    widgetDrinkList.appendChild(drinkCard)
                })

                // Show the recommendations section with animation
                widgetRecommendedDrinks.style.display = "block"
                widgetRecommendedDrinks.classList.add("animate__animated", "animate__fadeInUp")

                // Remove animation classes after animation completes
                setTimeout(() => {
                    widgetRecommendedDrinks.classList.remove("animate__animated", "animate__fadeInUp")
                }, 1000)
            } else {
                // Hide the recommendations section if no drinks
                widgetRecommendedDrinks.classList.add("animate__animated", "animate__fadeOutDown")

                setTimeout(() => {
                    widgetRecommendedDrinks.style.display = "none"
                    widgetRecommendedDrinks.classList.remove("animate__animated", "animate__fadeOutDown")
                }, 300)
            }
        }

        // Function to format price
        function formatWidgetPrice(price) {
            if (!price) return "0"
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")
        }

        // Function to send message to server
        function sendWidgetMessage() {
            if (!widgetUserInput) return

            const message = widgetUserInput.value.trim()

            if (message) {
                // Add user message to chat
                addWidgetMessage(message, true)

                // Clear input
                widgetUserInput.value = ""

                // Show typing indicator
                showWidgetTypingIndicator()

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
                                hideWidgetTypingIndicator()

                                // Add bot response to chat
                                addWidgetMessage(data.message, false)

                                // Display recommended drinks after a short delay
                                setTimeout(() => {
                                    displayWidgetRecommendedDrinks(data.recommendedDrinks)
                                }, 1000)
                            },
                            Math.max(1000, data.message.length * 20),
                        ) // Minimum 1 second, or longer for longer messages
                    })
                    .catch((error) => {
                        console.error("Error:", error)

                        // Hide typing indicator
                        setTimeout(() => {
                            hideWidgetTypingIndicator()

                            // Add error message
                            addWidgetMessage("Xin lỗi, đã xảy ra lỗi khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.", false)
                        }, 1000)
                    })
            }
        }

        // Event listeners
        if (widgetSendButton) {
            widgetSendButton.addEventListener("click", sendWidgetMessage)
        }

        if (widgetUserInput) {
            widgetUserInput.addEventListener("keypress", (e) => {
                if (e.key === "Enter") {
                    sendWidgetMessage()
                }
            })
        }

        // Add animation to initial message
        setTimeout(() => {
            const initialMessage = widgetMessages.querySelector(".bot-message")
            if (initialMessage) {
                initialMessage.classList.add("animate__animated", "animate__fadeIn")
            }
        }, 500)
    } else {
        console.log("Chat widget elements not found")
    }
})
