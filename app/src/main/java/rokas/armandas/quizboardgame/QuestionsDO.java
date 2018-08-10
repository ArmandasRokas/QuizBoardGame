package rokas.armandas.quizboardgame;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;


@DynamoDBTable(tableName = "quizboardgame-mobilehub-141398520-questions")

public class QuestionsDO {
    private String _questionId;
    private String _content;

    @DynamoDBHashKey(attributeName = "questionId")
    @DynamoDBAttribute(attributeName = "questionId")
    public String getQuestionId() {
        return _questionId;
    }

    public void setQuestionId(final String _questionId) {
        this._questionId = _questionId;
    }
    @DynamoDBAttribute(attributeName = "content")
    public String getContent() {
        return _content;
    }

    public void setContent(final String _content) {
        this._content = _content;
    }

}
