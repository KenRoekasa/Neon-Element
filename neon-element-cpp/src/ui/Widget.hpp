#pragma once

#include <SFML/Graphics.hpp>
#include <string>
#include <functional>

namespace neon {

// Base widget class for SFML UI elements
class Widget {
public:
    virtual ~Widget() = default;
    virtual void draw(sf::RenderWindow& window) = 0;
    virtual void handleEvent(const sf::Event& event, sf::RenderWindow& window) = 0;
    virtual bool contains(float x, float y) const = 0;

    void setPosition(float x, float y) { x_ = x; y_ = y; }
    void setVisible(bool v) { visible_ = v; }
    bool isVisible() const { return visible_; }

protected:
    float x_ = 0.f;
    float y_ = 0.f;
    bool visible_ = true;
};

// Neon-styled button
class Button : public Widget {
public:
    Button(const sf::Font& font, const std::string& text, unsigned charSize = 36);

    void draw(sf::RenderWindow& window) override;
    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    bool contains(float x, float y) const override;

    void setOnClick(std::function<void()> cb) { onClick_ = std::move(cb); }
    void setOnHover(std::function<void()> cb) { onHover_ = std::move(cb); }
    void setSize(float w, float h) { width_ = w; height_ = h; }
    void setColors(sf::Color normal, sf::Color hover, sf::Color pressed);
    sf::FloatRect getBounds() const;

private:
    sf::Text text_;
    sf::RectangleShape bg_;
    std::function<void()> onClick_;
    std::function<void()> onHover_;
    float width_ = 300.f;
    float height_ = 60.f;
    sf::Color normalColor_{80, 0, 120};
    sf::Color hoverColor_{140, 0, 200};
    sf::Color pressedColor_{200, 0, 255};
    bool hovered_ = false;
    bool wasHovered_ = false;
    bool pressed_ = false;
};

// Neon-styled label
class Label : public Widget {
public:
    Label(const sf::Font& font, const std::string& text, unsigned charSize = 24);

    void draw(sf::RenderWindow& window) override;
    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    bool contains(float /*x*/, float /*y*/) const override { return false; }

    void setText(const std::string& text) { text_.setString(text); }
    void setColor(sf::Color c) { text_.setFillColor(c); }
    void centerHorizontally(float windowWidth);

private:
    sf::Text text_;
};

// Neon-styled slider
class Slider : public Widget {
public:
    Slider(const sf::Font& font, const std::string& label,
           float minVal = 0.f, float maxVal = 100.f, float initial = 50.f);

    void draw(sf::RenderWindow& window) override;
    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    bool contains(float x, float y) const override;

    void setOnChange(std::function<void(float)> cb) { onChange_ = std::move(cb); }
    float getValue() const { return value_; }
    void setValue(float v);
    void setWidth(float w) { trackWidth_ = w; }

private:
    sf::Text label_;
    sf::Text valueText_;
    sf::RectangleShape track_;
    sf::RectangleShape fill_;
    sf::CircleShape knob_;
    std::function<void(float)> onChange_;
    float minVal_, maxVal_, value_;
    float trackWidth_ = 300.f;
    float trackHeight_ = 8.f;
    bool dragging_ = false;
};

// Radio button group
class RadioGroup : public Widget {
public:
    RadioGroup(const sf::Font& font, const std::string& groupLabel,
               const std::vector<std::string>& options, int defaultIndex = 0);

    void draw(sf::RenderWindow& window) override;
    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    bool contains(float x, float y) const override;

    int getSelectedIndex() const { return selectedIndex_; }
    void setSelectedIndex(int idx) { selectedIndex_ = idx; }
    void setOnChange(std::function<void(int)> cb) { onChange_ = std::move(cb); }

private:
    sf::Text groupLabel_;
    std::vector<sf::Text> optionTexts_;
    std::vector<sf::CircleShape> dots_;
    std::function<void(int)> onChange_;
    int selectedIndex_ = 0;
    float spacing_ = 30.f;
};

} // namespace neon
