package com.aigirls.model.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.service.battle.DamageCalculateService;
import com.badlogic.gdx.utils.Array;

public class EnemyBrainModel {
    private static final int DEFAULT_DEPTH = 2;
    private static final int WIN_SCORE = 10000;  //高めに設定しておく
    private static int[] ballHeightPenaltyForAi = {0, 0, 0, 0, 0, 0, 15, 30};
    private static int[] obstacleHeightPenaltyForAi = {-20, 0, 0, 0, 0, 30, 100, 300};
    private static int[] ballHeightPenaltyForPlayer = {0, 0, 0, 0, 0, 0, 10, 20};
    private static int[] obstacleHeightPenaltyForPlayer = {0, 0, 0, 0, 0, 0, 50, 200};
    private static int[] decreaseHpPenalty = {0, 60, 120, 300, 500, 1000};
    private static int[] decreaseHpPenaltyForPlayer = {0, 80, 170, 500, 1300, 2000};  //攻撃してほしいので高めに設定
    private static int[] ballStackScores = {-200, -100, 0, 0, 0, 0};//{-80, -30, 0, 15, 30, 60};
    private static int[] FutureBeatScores = {2000, 1500, 1200, 800, 500};
    private static final int MAGNIFICATION_FOR_ATTACK = 200;
    private static final int IMPPOSIBLE_BEAT = 1000;
    private static final int BALL_STACK_MIN_NUM = 1;
    private int depth;
    private int[][] oneBalls = {
            {0}, {1}, {2}, {3}, {4}, {5}, {6},
            {0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, {0,6},
            {1,1}, {1,2}, {1,3}, {1,4}, {1,5}, {1,6},
            {2,2}, {2,3}, {2,4}, {2,5}, {2,6},
            {3,3}, {3,4}, {3,5}, {3,6},
            {4,4}, {4,5}, {4,6},
            {5,5}, {5,6},
            {6,6},
            {0,0,0}, {1,1,1}, {2,2,2}, {3,3,3}, {4,4,4}, {5,5,5}, {6,6,6},
            {0,0,1}, {1,1,2}, {2,2,3}, {3,3,4}, {4,4,5}, {5,5,6}, {4,5,6},
            {0,0,2}, {0,1,2}, {2,2,4}, {2,2,5}, {2,2,6}, {5,5,4}, {6,6,1},
            {0,0,3}, {1,1,3}, {1,1,4}, {2,2,1}, {2,2,0}, {4,4,0}, {4,4,1},
            {0,0,4}, {1,1,0}, {3,3,0}, {3,3,1}, {3,3,6}, {4,4,2}, {4,4,3},
            {0,0,5}, {1,1,5}, {3,3,2}, {3,3,5}, {4,4,6}, {5,5,0}, {5,5,1},
            {0,0,6}, {1,1,6}, {5,5,2}, {5,5,3}, {6,6,0}, {6,6,2}, {6,6,3},
            {6,6,4}, {6,6,5}, {1,2,3}, {2,3,4}, {3,4,5}, {0,1,3}, {0,1,4},
            {0,1,5}, {0,1,6}, {0,2,3}, {0,2,4}, {0,2,5}, {0,2,6}, {0,3,4},
            {0,3,5}, {0,3,6}, {0,4,5}, {0,4,6}, {0,5,6}, {1,2,4}, {1,2,5},
            {1,2,6}, {1,3,4}, {1,3,5}, {1,3,6}, {1,4,5}, {1,4,6}, {1,5,6},
            {2,3,5}, {2,3,6}, {2,4,5}, {2,4,6}, {2,5,6}, {3,4,6}, {3,5,6},
        };
    private int[][] tempPuts;
    private int totalBallCount = 0;


    public EnemyBrainModel()
    {
        this(DEFAULT_DEPTH);
    }

    public EnemyBrainModel(int d)
    {
        depth = d;
    }

    public void settTotalBallCount(int count)
    {
        totalBallCount = count;
    }

    public EnemyActionModel decideAction(CharacterModel ai, CharacterModel player)
    {
        Array<int[]> array = new Array<int[]>(oneBalls);
        array.shuffle();
        tempPuts = array.toArray(int[].class);
        return alphabeta(ai.getClone(), player.getClone(), depth, null);
    }

    private EnemyActionModel alphabeta(CharacterModel attacker, CharacterModel defender, int depth, Integer currentScoreObject)
    {
        int[][] oneBalls = tempPuts;
        boolean first = (this.depth-depth)%2 == 0;
        if (depth == 0) {
            CharacterModel[] characters = getCharacterModels(attacker, defender, first);
            return new EnemyActionModel(new int[0], null, new BallInfoModel[0], getScore(characters[0], characters[1], first));
        }

        int currentScore;
        if (currentScoreObject != null) {
            currentScore = currentScoreObject.intValue();
        } else {
            currentScore = first ? -1*WIN_SCORE : WIN_SCORE;
        }

        //スタックへのボール追加
        if (depth != this.depth) {
            attacker.addBallToStack(1);
        }

        int score = alphabeta(defender, attacker, depth-1, null).score;
        EnemyActionModel currentAction = new EnemyActionModel(new int[0], null, new BallInfoModel[0], score);
        if (canShortCut(first, currentScore, currentAction.score)) {
            return currentAction;
        }

        int ballNumToUse = attacker.getStackedBallNum() - BALL_STACK_MIN_NUM;
        int[] ballHeights = getBallHeights(attacker);
        for (int i=0; i<oneBalls.length ; i++) {
            if (ballNumToUse < oneBalls[i].length || !isPutBalls(oneBalls[i], ballHeights)) {
                continue;
            }
            CharacterModel attackerClone = attacker.getClone();
            CharacterModel defenderClone = defender.getClone();
            simulatePutBall(attackerClone, defenderClone, oneBalls[i]);
            //いろいろきついので一番強い技だけを出すようにする
            currentAction = getBetterAction(first, oneBalls[i], null, new BallInfoModel[0], alphabeta(defenderClone, attackerClone, depth-1, currentAction.score), currentAction, 0);
            if (canShortCut(first, currentScore, currentAction.score)) {
                return currentAction;
            }
            ActiveMagicModel magicToUse = getBestMagic(attackerClone);
            if (magicToUse == null) {
                continue;
            }
            int length = magicToUse.getNumTargetBalls();
            for (int index = 0 ; index < length; index++) {
                CharacterModel attackerCloneClone = attackerClone.getClone();
                CharacterModel defenderCloneClone = defenderClone.getClone();
                BallInfoModel[] targetBalls = magicToUse.getBallInfoModels(index);
                int num = attackerCloneClone.getStackedBallNum();
                int scoreForAttack = (int) Math.round(GameConfig.STATUS_RATES[num] * magicToUse.getStrength() * MAGNIFICATION_FOR_ATTACK);
                getScoreAfterAction(attackerCloneClone, defenderCloneClone, magicToUse, targetBalls);
                currentAction = getBetterAction(
                        first,
                        oneBalls[i],
                        magicToUse,
                        targetBalls,
                        alphabeta(defenderCloneClone, attackerCloneClone, depth-1, currentAction.score),
                        currentAction,
                        scoreForAttack);
                if (canShortCut(first, currentScore, currentAction.score)) {
                    return currentAction;
                }
            }
        }
        return currentAction;
    }

    private boolean canShortCut(boolean first, int currentScore, int targetScore) {
        return (first && targetScore < currentScore) || (!first && targetScore > currentScore);
    }

    private EnemyActionModel getBetterAction(boolean attack, int[] puts, ActiveMagicModel magic, BallInfoModel[] balls, EnemyActionModel futureAction, EnemyActionModel currentAction, int scoreForAttack)
    {
        EnemyActionModel action = new EnemyActionModel(puts, magic, balls, futureAction.score);
        int score = action.score + scoreForAttack;
        if (attack && currentAction.score < score) {
            currentAction = action;
        } else if (!attack && currentAction.score > score) {
            currentAction = action;
        }
        return currentAction;
    }

    private ActiveMagicModel getBestMagic(CharacterModel chara){
        ActiveMagicModel magicToUse = null;

        ActiveMagicModel[] magics = chara.getActiveMagicModels();
        for (ActiveMagicModel m : magics) {
            if (m.canOutbreak() && (magicToUse == null || m.getStrength() > magicToUse.getStrength())) {
                magicToUse = m;
            }
        }
        return magicToUse;
    }

    private CharacterModel[] getCharacterModels(CharacterModel attacker, CharacterModel defender, boolean attack)
    {
        //AI,Playerの順番
        CharacterModel[] models = new CharacterModel[2];
        if (attack) {
            models[0] = attacker;
            models[1] = defender;
        } else {
            models[0] = defender;
            models[1] = attacker;
        }
        return models;
    }

    private int[] getBallHeights(CharacterModel chara)
    {
        int[] heights = new int[GameConfig.BOARD_WIDTH];
        for (int x=0; x<heights.length; x++) {
            int dropPlace = chara.getDropPlace(x);
            if (dropPlace == BoardModel.CAN_NOT_SET_BALL) {
                dropPlace = GameConfig.BOARD_HEIGHT;
            }
            heights[x] = dropPlace;
        }
        return heights;
    }

    private boolean isPutBalls(int[] puts, int[] ballHeights)
    {
        int[] heights = new int[ballHeights.length];
        for (int i=0; i<heights.length; i++) {
            heights[i] = ballHeights[i];
        }
        for (int p : puts) {
            heights[p]++;
            if (heights[p] >= GameConfig.BOARD_HEIGHT) {
                return false;
            }
        }
        return true;
    }

    private int getScore(CharacterModel ai, CharacterModel player, boolean first)
    {
        if (!player.isAlive()) {
            return WIN_SCORE;
        }
        if (!ai.isAlive()) {
            return -1*WIN_SCORE;
        }
        int scoreForAI = calculateBoardScoreForAi(ai) + calculateHpScoreForAi(ai) + calculateBallStackScore(ai); //+ calculateAttackScore(ai);
        int scoreForPlayer = -1*calculateBoardScoreForPlayer(player) - calculateHpScoreForPlayer(player) - calculateBallStackScore(player); //- calculateAttackScore(player);
        int futureScore = calculateFutureScore(ai, player, first); //TODO 本当に使うかどうか速度と相談
        return scoreForAI + scoreForPlayer + futureScore;
    }

    private int calculateAttackScore(CharacterModel chara)
    {
        ActiveMagicModel magic = getBestMagic(chara);
        if (magic == null) {
            return 0;
        }
        int num = chara.getStackedBallNum();
        return (int) Math.round(GameConfig.STATUS_RATES[num] * magic.getStrength() * MAGNIFICATION_FOR_ATTACK);
    }

    private int calculateBoardScoreForAi(CharacterModel chara)
    {
        return calculateBoardScore(chara, ballHeightPenaltyForAi, obstacleHeightPenaltyForAi);
    }

    private int calculateBoardScoreForPlayer(CharacterModel chara)
    {
        return calculateBoardScore(chara, ballHeightPenaltyForPlayer, obstacleHeightPenaltyForPlayer);
    }

    /**
     * ボードの評価値を返す
     * @param chara
     * @return
     */
    private int calculateBoardScore(CharacterModel chara, int[] ballHeightPenalty, int[] obstacleHeightPenalty)
    {
        int total = 0;
        for (int x = 0; x < GameConfig.BOARD_WIDTH; x++) {
            int[] counts = chara.getBallCounts(x);
            total += ballHeightPenalty[counts[0]];
            total += obstacleHeightPenalty[counts[1]];
        }
        return -1*total;
    }

    private int calculateHpScoreForAi(CharacterModel chara) {
        return calculateHpScore(chara, decreaseHpPenalty);
    }

    private int calculateHpScoreForPlayer(CharacterModel chara) {
        return calculateHpScore(chara, decreaseHpPenaltyForPlayer);
    }

    /**
     * 残りHPのに関する評価値を返す
     * @param chara
     * @return
     */
    private int calculateHpScore(CharacterModel chara, int[] decreaseHpPenalty)
    {
        double rate = 1 - 1.0*chara.getCurrentHp()/chara.getMaxHp();
        if (rate >= 1) {
            return decreaseHpPenalty[decreaseHpPenalty.length-1];
        }
        int index = (int) Math.floor((decreaseHpPenalty.length-1)*rate);
        int minPenalty = decreaseHpPenalty[index];
        int maxPenalty = decreaseHpPenalty[index+1];
        return (int) Math.round(-1.0 * ((maxPenalty-minPenalty) * ((decreaseHpPenalty.length-1)*rate-index) + minPenalty));
    }

    /**
     * ボールスタック数に関する評価値を返す
     *
     */
    private int calculateBallStackScore(CharacterModel chara)
    {
        return ballStackScores[chara.getStackedBallNum()];
    }

    /**
     * 未来で確実に負ける(勝つ)かどうかを調べて、結果を評価値として取得
     * どちらもこのターンで死んでないことが前提
     */
    private int calculateFutureScore(CharacterModel ai, CharacterModel player, boolean first)
    {
        //AI側
        int turnForAi = getTurnNumUntilBeating(ai, player);
        //プレイヤー側
        int turnForPlayer = getTurnNumUntilBeating(player, ai);

        if (turnForAi < turnForPlayer) {
            return FutureBeatScores[turnForAi-1];
        } else if (turnForPlayer < turnForAi) {
            return -1*FutureBeatScores[turnForPlayer-1];
        } else if (turnForAi != IMPPOSIBLE_BEAT) {
            return first ? FutureBeatScores[turnForAi-1] : -1*FutureBeatScores[turnForPlayer-1];
        }
        return 0;
    }

    /**
     * 相手を確実に倒すまでにかかるターン数を返す
     * @param chara
     * @return
     */
    private int getTurnNumUntilBeating(CharacterModel attacker, CharacterModel defender)
    {
        int currentStack = attacker.getStackedBallNum();
        ActiveMagicModel[] magics = attacker.getActiveMagicModels();
        for (int i = 1; i <= GameConfig.LIMIT_STACKED_BALL_NUM-currentStack; i++) {
            attacker.addBallToStack(1); //スタックにボールの追加(後で必ず消すこと)
            for (ActiveMagicModel magic : magics) {
                if (!magic.canOutbreak()) {
                    continue;
                }
                int damege = DamageCalculateService.getDamageValue( (int) Math.round(magic.getAttackRate() * attacker.getAttack()), defender.getDefense());
                if (damege >= defender.getDefense()) {
                    return i;
                }
            }
        }
        for (int i = 1; i <= GameConfig.LIMIT_STACKED_BALL_NUM-currentStack; i++) {
            attacker.removeBallFromStack();
        }
        return IMPPOSIBLE_BEAT;
    }

    /**
     * ボール配置のシミュレーション
     */
    private void simulatePutBall(CharacterModel attacker, CharacterModel defender, int[] puts)
    {
        for (int i=0; i<puts.length; i++) {
            int x = puts[i];
            attacker.removeBallFromStack();
            attacker.setBall(x, new BallModel(totalBallCount+(i+1)));
            int defenserYPlace = defender.getDropPlace(x);
            if (defenserYPlace == BoardModel.CAN_NOT_SET_BALL) {
                int damage = DamageCalculateService.getDamageValue(
                    (int)Math.round(GameConfig.DIRECT_ATTACK_RATE*attacker.getAttack()),
                    defender.getDefense());
                defender.beHurt(damage);
            } else {
                defender.setBall(x, new ObstacleBallModel(totalBallCount+(i+1), attacker.getMagicDefense()));
            }
        }
    }

    /**
     * 攻撃時のシミュレーション
     * @param attacker
     * @param defender
     * @param puts
     */
    private void getScoreAfterAction(CharacterModel attacker, CharacterModel defender, ActiveMagicModel magic, BallInfoModel[] targetBalls)
    {
        int damage = DamageCalculateService.getDamageValue( (int) Math.round(magic.getAttackRate() * attacker.getAttack()), defender.getDefense());
        int damageToBall = (int) Math.round(magic.getBallAttackRate() * attacker.getMagicAttack());
        attacker.outbreakMagic(damageToBall, targetBalls);
        defender.beHurt(damage);
        attacker.addBallToStack(magic.getRecoverBall());
    }

}
