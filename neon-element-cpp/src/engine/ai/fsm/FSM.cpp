#include "FSM.hpp"
#include "engine/ai/controller/AiController.hpp"
#include "engine/ai/enums/AiStates.hpp"

namespace neon {

FSM::FSM(Player* aiPlayer, AiController& aiCon, AiCalculations& calc)
    : aiPlayer_(aiPlayer), aiCon_(aiCon), maxHP_(Player::kMaxHealth),
      puCalc_(calc.getPowerupCalc()), playerCalc_(calc.getPlayerCalc())
{
}

std::unique_ptr<FSM> FSM::create(Player* aiPlayer, AiCalculations& calc,
                                  GameType& gameType, AiController& aiCon)
{
    switch (gameType.getType()) {
        case GameType::Type::Timed:
            return std::make_unique<TimedFSM>(aiPlayer, aiCon, calc);
        case GameType::Type::FirstToXKills:
            return std::make_unique<KillsFSM>(aiPlayer, aiCon, calc);
        case GameType::Type::Hill:
            return std::make_unique<HillFSM>(aiPlayer, aiCon, calc);
        case GameType::Type::Regicide:
            return std::make_unique<RegicideFSM>(aiPlayer, aiCon, calc);
    }
    return std::make_unique<KillsFSM>(aiPlayer, aiCon, calc);
}

void FSM::fetchAction()
{
    switch (aiCon_.getAiType()) {
        case AiType::Easy:   easyAiFetchAction(); break;
        case AiType::Normal: normalAiFetchAction(); break;
        case AiType::Hard:   hardAiFetchAction(); break;
    }
}

// --- KillsFSM ---

void KillsFSM::easyAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (puCalc_.powerupCloserThanPlayer() && hp < maxHP_ && puCalc_.getNearestPowerUp(PowerUpType::Heal) != -1)
        aiCon_.setState(AiState::FindHealth);
    else if (pHP < maxHP_ / 3.f && hp > pHP)
        aiCon_.setState(AiState::AggressiveAttack);
    else if (hp < maxHP_ / 10.f || pHP - hp > 20.f)
        aiCon_.setState(AiState::Escape);
    else if (playerCalc_.playerIsTooClose() || hp > pHP)
        aiCon_.setState(AiState::Attack);
    else if (puCalc_.powerupIsTooClose()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else
        aiCon_.setState(AiState::Wander);
}

void KillsFSM::normalAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if ((puCalc_.powerupIsTooClose() && puCalc_.powerupCloserThanPlayer()) || puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ / 3.f && pHP - hp > 20.f)
        aiCon_.setState(AiState::Escape);
    else if (pHP < maxHP_ / 3.f)
        aiCon_.setState(AiState::AggressiveAttack);
    else if (playerCalc_.playerIsTooClose() || hp > pHP)
        aiCon_.setState(AiState::Attack);
    else
        aiCon_.setState(AiState::Wander);
}

void KillsFSM::hardAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ && puCalc_.powerUpExist(PowerUpType::Heal))
        aiCon_.setState(AiState::FindHealth);
    else if (puCalc_.powerupCloserThanPlayer() && puCalc_.getNearestPowerUp(PowerUpType::Damage) != -1)
        aiCon_.setState(AiState::FindDamage);
    else if ((hp < maxHP_ / 2.f && hp < pHP) || playerCalc_.someoneCloseIsCharging() ||
             playerCalc_.isCharging(*nearest) ||
             (playerCalc_.getWinningPlayer() == aiPlayer_ && pHP - hp > 20.f))
        aiCon_.setState(AiState::Escape);
    else if (playerCalc_.getPlayerWithLowestHealth() &&
             playerCalc_.getPlayerWithLowestHealth()->getHealth() < 20.f &&
             playerCalc_.getPlayerWithLowestHealth()->getHealth() > 0.f)
        aiCon_.setState(AiState::AttackLosing);
    else if (pHP < maxHP_ / 2.f || aiPlayer_->activeDamagePowerup())
        aiCon_.setState(AiState::AggressiveAttack);
    else if (playerCalc_.scoreDifferenceIsMoreThan(2))
        aiCon_.setState(AiState::AttackWinner);
    else if (hp > pHP)
        aiCon_.setState(AiState::Attack);
    else if (puCalc_.getNearestPowerUp(PowerUpType::Speed) != -1)
        aiCon_.setState(AiState::FindSpeed);
    else
        aiCon_.setState(AiState::Attack);
}

// --- TimedFSM --- (similar to KillsFSM with minor differences)

void TimedFSM::easyAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (puCalc_.powerupCloserThanPlayer() && hp < maxHP_ && puCalc_.getNearestPowerUp(PowerUpType::Heal) != -1)
        aiCon_.setState(AiState::FindHealth);
    else if (pHP < maxHP_ / 3.f && hp > pHP)
        aiCon_.setState(AiState::AggressiveAttack);
    else if (hp < maxHP_ / 10.f || pHP - hp > 20.f)
        aiCon_.setState(AiState::Escape);
    else if (playerCalc_.playerIsTooClose() || hp > pHP)
        aiCon_.setState(AiState::Attack);
    else
        aiCon_.setState(AiState::Wander);
}

void TimedFSM::normalAiFetchAction()
{
    KillsFSM temp(aiPlayer_, aiCon_, *static_cast<AiCalculations*>(nullptr));
    // Delegate to kills normal for simplicity (same logic)
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ / 3.f && pHP - hp > 20.f)
        aiCon_.setState(AiState::Escape);
    else if (pHP < maxHP_ / 3.f)
        aiCon_.setState(AiState::AggressiveAttack);
    else if (playerCalc_.playerIsTooClose() || hp > pHP)
        aiCon_.setState(AiState::Attack);
    else
        aiCon_.setState(AiState::Wander);
}

void TimedFSM::hardAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ && puCalc_.powerUpExist(PowerUpType::Heal))
        aiCon_.setState(AiState::FindHealth);
    else if ((hp < maxHP_ / 2.f && hp < pHP) || playerCalc_.someoneCloseIsCharging())
        aiCon_.setState(AiState::Escape);
    else if (pHP < maxHP_ / 2.f || aiPlayer_->activeDamagePowerup())
        aiCon_.setState(AiState::AggressiveAttack);
    else if (hp > pHP)
        aiCon_.setState(AiState::Attack);
    else
        aiCon_.setState(AiState::Attack);
}

// --- HillFSM ---

HillFSM::HillFSM(Player* aiPlayer, AiController& aiCon, AiCalculations& calc)
    : FSM(aiPlayer, aiCon, calc),
      hillCalc_(static_cast<HillCalculations&>(calc))
{
}

void HillFSM::easyAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (hp < maxHP_ / 10.f)
        aiCon_.setState(AiState::Escape);
    else if (hillCalc_.onHill()) {
        Player* onHill = hillCalc_.getOnHillPlayer();
        if (onHill)
            aiCon_.setState(AiState::Attack);
        else
            aiCon_.setState(AiState::WanderOnHill);
    }
    else if (pHP < maxHP_ / 3.f && hp > pHP)
        aiCon_.setState(AiState::AggressiveAttack);
    else
        aiCon_.setState(AiState::GoToHill);
}

void HillFSM::normalAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ / 3.f)
        aiCon_.setState(AiState::Escape);
    else if (hillCalc_.onHill()) {
        Player* onHill = hillCalc_.getOnHillPlayer();
        if (onHill)
            aiCon_.setState(AiState::AggressiveAttack);
        else
            aiCon_.setState(AiState::WanderOnHill);
    }
    else
        aiCon_.setState(AiState::GoToHill);
}

void HillFSM::hardAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;

    if (puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ && puCalc_.powerUpExist(PowerUpType::Heal))
        aiCon_.setState(AiState::FindHealth);
    else if (hp < maxHP_ / 2.f || playerCalc_.someoneCloseIsCharging())
        aiCon_.setState(AiState::Escape);
    else if (hillCalc_.onHill()) {
        Player* onHill = hillCalc_.getOnHillPlayer();
        if (onHill)
            aiCon_.setState(AiState::AggressiveAttack);
        else
            aiCon_.setState(AiState::WanderOnHill);
    }
    else
        aiCon_.setState(AiState::GoToHill);
}

// --- RegicideFSM ---

RegicideFSM::RegicideFSM(Player* aiPlayer, AiController& aiCon, AiCalculations& calc)
    : FSM(aiPlayer, aiCon, calc),
      regCalc_(static_cast<RegicideCalculations&>(calc))
{
}

void RegicideFSM::easyAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (hp < maxHP_ / 10.f)
        aiCon_.setState(AiState::Escape);
    else if (regCalc_.kingIsClose())
        aiCon_.setState(AiState::AttackKing);
    else if (pHP < maxHP_ / 3.f && hp > pHP)
        aiCon_.setState(AiState::AggressiveAttack);
    else if (playerCalc_.playerIsTooClose() || hp > pHP)
        aiCon_.setState(AiState::Attack);
    else
        aiCon_.setState(AiState::Wander);
}

void RegicideFSM::normalAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;
    float pHP = nearest->getHealth();

    if (puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ / 3.f)
        aiCon_.setState(AiState::Escape);
    else if (regCalc_.kingIsClose())
        aiCon_.setState(AiState::AttackKing);
    else if (pHP < maxHP_ / 3.f)
        aiCon_.setState(AiState::AggressiveAttack);
    else if (playerCalc_.playerIsTooClose() || hp > pHP)
        aiCon_.setState(AiState::Attack);
    else
        aiCon_.setState(AiState::AttackKing);
}

void RegicideFSM::hardAiFetchAction()
{
    float hp = aiPlayer_->getHealth();
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return;

    if (puCalc_.powerupCloserThanPlayer()) {
        int idx = puCalc_.getNearestPowerUp();
        if (idx >= 0) {
            auto& pups = puCalc_.getPowerups();
            switch (pups[static_cast<size_t>(idx)]->getType()) {
                case PowerUpType::Damage: aiCon_.setState(AiState::FindDamage); break;
                case PowerUpType::Heal:   aiCon_.setState(AiState::FindHealth); break;
                case PowerUpType::Speed:  aiCon_.setState(AiState::FindSpeed); break;
            }
        }
    }
    else if (hp < maxHP_ && puCalc_.powerUpExist(PowerUpType::Heal))
        aiCon_.setState(AiState::FindHealth);
    else if (hp < maxHP_ / 2.f || playerCalc_.someoneCloseIsCharging())
        aiCon_.setState(AiState::Escape);
    else if (regCalc_.kingIsClose())
        aiCon_.setState(AiState::AttackKing);
    else if (aiPlayer_->activeDamagePowerup())
        aiCon_.setState(AiState::AggressiveAttack);
    else
        aiCon_.setState(AiState::AttackKing);
}

} // namespace neon
