package codexe.han.slack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackActionDTO {
    /**
     "attachments": [
     {
     "fallback": "Book your flights at https://flights.example.com/book/r123456",
     "actions": [
     {
     "type": "button",
     "text": "Book flights 🛫",
     "url": "https://flights.example.com/book/r123456"
     }
     ]
     }
     ]
     */
    private String type;

    private String text;

    private String url;
}
