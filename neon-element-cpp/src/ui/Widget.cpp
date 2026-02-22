#include "Widget.hpp"
#include <cmath>
#include <sstream>
#include <iomanip>

namespace neon {

// ============================================================
// Button
// ============================================================

Button::Button(const sf::Font& font, const std::string& text, unsigned charSize)
{
    text_.setFont(font);
    text_.setString(text);
    text_.setCharacterSize(charSize);
    text_.setFillColor(sf::Color::White);
}

void Button::draw(sf::RenderWindow& window)
{
    if (!visible_) return;

    bg_.setSize({width_, height_});
    bg_.setPosition(x_ - width_ / 2.f, y_ - height_ / 2.f);

    if (pressed_)
        bg_.setFillColor(pressedColor_);
    else if (hovered_)
        bg_.setFillColor(hoverColor_);
    else
        bg_.setFillColor(normalColor_);

    bg_.setOutlineColor(sf::Color(255, 0, 255, hovered_ ? 255 : 150));
    bg_.setOutlineThickness(2.f);

    // Center text in button
    sf::FloatRect bounds = text_.getLocalBounds();
    text_.setOrigin(bounds.left + bounds.width / 2.f, bounds.top + bounds.height / 2.f);
    text_.setPosition(x_, y_);

    window.draw(bg_);
    window.draw(text_);
}

void Button::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!visible_) return;

    sf::Vector2i mousePos = sf::Mouse::getPosition(window);
    float mx = static_cast<float>(mousePos.x);
    float my = static_cast<float>(mousePos.y);
    hovered_ = contains(mx, my);

    if (hovered_ && !wasHovered_ && onHover_) {
        onHover_();
    }
    wasHovered_ = hovered_;

    if (event.type == sf::Event::MouseButtonPressed && event.mouseButton.button == sf::Mouse::Left) {
        if (hovered_) pressed_ = true;
    }
    if (event.type == sf::Event::MouseButtonReleased && event.mouseButton.button == sf::Mouse::Left) {
        if (pressed_ && hovered_ && onClick_) {
            onClick_();
        }
        pressed_ = false;
    }
}

bool Button::contains(float x, float y) const
{
    float left = x_ - width_ / 2.f;
    float top = y_ - height_ / 2.f;
    return x >= left && x <= left + width_ && y >= top && y <= top + height_;
}

void Button::setColors(sf::Color normal, sf::Color hover, sf::Color pressed)
{
    normalColor_ = normal;
    hoverColor_ = hover;
    pressedColor_ = pressed;
}

sf::FloatRect Button::getBounds() const
{
    return {x_ - width_ / 2.f, y_ - height_ / 2.f, width_, height_};
}

// ============================================================
// Label
// ============================================================

Label::Label(const sf::Font& font, const std::string& text, unsigned charSize)
{
    text_.setFont(font);
    text_.setString(text);
    text_.setCharacterSize(charSize);
    text_.setFillColor(sf::Color(255, 0, 255));
}

void Label::draw(sf::RenderWindow& window)
{
    if (!visible_) return;
    text_.setPosition(x_, y_);
    window.draw(text_);
}

void Label::handleEvent(const sf::Event& /*event*/, sf::RenderWindow& /*window*/)
{
    // Labels are non-interactive
}

void Label::centerHorizontally(float windowWidth)
{
    sf::FloatRect bounds = text_.getLocalBounds();
    x_ = (windowWidth - bounds.width) / 2.f - bounds.left;
}

// ============================================================
// Slider
// ============================================================

Slider::Slider(const sf::Font& font, const std::string& label,
               float minVal, float maxVal, float initial)
    : minVal_(minVal), maxVal_(maxVal), value_(initial)
{
    label_.setFont(font);
    label_.setString(label);
    label_.setCharacterSize(20);
    label_.setFillColor(sf::Color(200, 200, 200));

    valueText_.setFont(font);
    valueText_.setCharacterSize(18);
    valueText_.setFillColor(sf::Color::White);

    track_.setFillColor(sf::Color(40, 0, 60));
    track_.setOutlineColor(sf::Color(100, 0, 150));
    track_.setOutlineThickness(1.f);

    fill_.setFillColor(sf::Color(200, 0, 255));

    knob_.setRadius(10.f);
    knob_.setFillColor(sf::Color(255, 0, 255));
    knob_.setOrigin(10.f, 10.f);
}

void Slider::draw(sf::RenderWindow& window)
{
    if (!visible_) return;

    // Label
    label_.setPosition(x_, y_);
    window.draw(label_);

    // Track
    float trackY = y_ + 30.f;
    track_.setSize({trackWidth_, trackHeight_});
    track_.setPosition(x_, trackY);
    window.draw(track_);

    // Fill
    float ratio = (value_ - minVal_) / (maxVal_ - minVal_);
    fill_.setSize({trackWidth_ * ratio, trackHeight_});
    fill_.setPosition(x_, trackY);
    window.draw(fill_);

    // Knob
    knob_.setPosition(x_ + trackWidth_ * ratio, trackY + trackHeight_ / 2.f);
    window.draw(knob_);

    // Value text
    std::ostringstream oss;
    oss << std::fixed << std::setprecision(0) << value_;
    valueText_.setString(oss.str());
    valueText_.setPosition(x_ + trackWidth_ + 15.f, trackY - 5.f);
    window.draw(valueText_);
}

void Slider::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!visible_) return;

    sf::Vector2i mousePos = sf::Mouse::getPosition(window);
    float mx = static_cast<float>(mousePos.x);
    float my = static_cast<float>(mousePos.y);

    float trackY = y_ + 30.f;

    if (event.type == sf::Event::MouseButtonPressed && event.mouseButton.button == sf::Mouse::Left) {
        if (mx >= x_ && mx <= x_ + trackWidth_ &&
            my >= trackY - 12.f && my <= trackY + trackHeight_ + 12.f) {
            dragging_ = true;
        }
    }
    if (event.type == sf::Event::MouseButtonReleased) {
        dragging_ = false;
    }
    if (dragging_) {
        float ratio = (mx - x_) / trackWidth_;
        if (ratio < 0.f) ratio = 0.f;
        if (ratio > 1.f) ratio = 1.f;
        value_ = minVal_ + ratio * (maxVal_ - minVal_);
        if (onChange_) onChange_(value_);
    }
}

bool Slider::contains(float x, float y) const
{
    float trackY = y_ + 30.f;
    return x >= x_ && x <= x_ + trackWidth_ &&
           y >= trackY - 12.f && y <= trackY + trackHeight_ + 12.f;
}

void Slider::setValue(float v)
{
    if (v < minVal_) v = minVal_;
    if (v > maxVal_) v = maxVal_;
    value_ = v;
}

// ============================================================
// RadioGroup
// ============================================================

RadioGroup::RadioGroup(const sf::Font& font, const std::string& groupLabel,
                       const std::vector<std::string>& options, int defaultIndex)
    : selectedIndex_(defaultIndex)
{
    groupLabel_.setFont(font);
    groupLabel_.setString(groupLabel);
    groupLabel_.setCharacterSize(22);
    groupLabel_.setFillColor(sf::Color(255, 0, 255));

    for (const auto& opt : options) {
        sf::Text t;
        t.setFont(font);
        t.setString(opt);
        t.setCharacterSize(20);
        t.setFillColor(sf::Color(200, 200, 200));
        optionTexts_.push_back(t);

        sf::CircleShape dot(8.f);
        dot.setFillColor(sf::Color(40, 0, 60));
        dot.setOutlineColor(sf::Color(150, 0, 200));
        dot.setOutlineThickness(2.f);
        dot.setOrigin(8.f, 8.f);
        dots_.push_back(dot);
    }
}

void RadioGroup::draw(sf::RenderWindow& window)
{
    if (!visible_) return;

    groupLabel_.setPosition(x_, y_);
    window.draw(groupLabel_);

    for (size_t i = 0; i < optionTexts_.size(); i++) {
        float optY = y_ + 30.f + static_cast<float>(i) * spacing_;

        dots_[i].setPosition(x_ + 10.f, optY + 10.f);
        if (static_cast<int>(i) == selectedIndex_)
            dots_[i].setFillColor(sf::Color(255, 0, 255));
        else
            dots_[i].setFillColor(sf::Color(40, 0, 60));
        window.draw(dots_[i]);

        optionTexts_[i].setPosition(x_ + 28.f, optY);
        window.draw(optionTexts_[i]);
    }
}

void RadioGroup::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!visible_) return;

    if (event.type == sf::Event::MouseButtonPressed && event.mouseButton.button == sf::Mouse::Left) {
        sf::Vector2i mousePos = sf::Mouse::getPosition(window);
        float mx = static_cast<float>(mousePos.x);
        float my = static_cast<float>(mousePos.y);

        for (size_t i = 0; i < optionTexts_.size(); i++) {
            float optY = y_ + 30.f + static_cast<float>(i) * spacing_;
            if (mx >= x_ && mx <= x_ + 200.f && my >= optY && my <= optY + spacing_) {
                selectedIndex_ = static_cast<int>(i);
                if (onChange_) onChange_(selectedIndex_);
                break;
            }
        }
    }
}

bool RadioGroup::contains(float x, float y) const
{
    float totalH = 30.f + static_cast<float>(optionTexts_.size()) * spacing_;
    return x >= x_ && x <= x_ + 200.f && y >= y_ && y <= y_ + totalH;
}

} // namespace neon
