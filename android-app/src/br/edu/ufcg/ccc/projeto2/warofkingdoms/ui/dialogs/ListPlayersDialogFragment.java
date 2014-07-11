package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.adapter.CustomListViewAdapter;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.entities.HouseToken;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RowItem;
import br.ufcg.edu.ccc.projeto2.R;

public class ListPlayersDialogFragment extends DialogFragment implements OnClickListener {

	private Button okBtn;
	private ListView resultList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.list_players_dialog, null, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        
        
        resultList = (ListView) view.findViewById(R.id.list);
        okBtn = (Button) view.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);
        
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		List<RowItem> rowItems = new ArrayList<RowItem>();
		
		GameManager gameManager = GameManager.getInstance();
		HouseTokenManager houseTokenManager = HouseTokenManager.getInstance();
		
		List<Player> players = gameManager.getCurrentPlayers();
		
		for (Player player : players) {
			HouseToken playerHouseToken = houseTokenManager
					.getHouseToken(player.getHouse());
			String playerName = player.getName();
			if (player.equals(gameManager.getCurrentPlayer())) {
				playerName = playerName + " (You)";
			}
			RowItem rowItem = new RowItem(playerHouseToken.getCastleToken(), playerName);
			rowItems.add(rowItem);
		}
		CustomListViewAdapter adapter = new CustomListViewAdapter(getActivity(),
				R.layout.list_players_item, rowItems);

		resultList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == okBtn) {
			dismiss();
		}
	}

}
