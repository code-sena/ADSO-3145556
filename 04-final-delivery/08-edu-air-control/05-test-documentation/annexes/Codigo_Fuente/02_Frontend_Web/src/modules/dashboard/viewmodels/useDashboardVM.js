import { useState, useMemo } from 'react';
import { calcScore } from '../../environment/utils/environmentHelpers';
import { useEnvironments } from '../../../context/useEnvironment';
import apiClient from '../../../shared/services/apiClient';

const STATUS_KEY_MAP = {
  normal: 'dashboard.statusNormal',
  warning: 'dashboard.statusWarning',
  alert: 'dashboard.statusAlert',
};

export function useDashboardVM() {
  const { environments, toggleFavorite } = useEnvironments();
  const [filter, setFilter] = useState('all');
  const [dashboardData, setDashboardData] = useState(null);
  const [series, setSeries] = useState([]);
  const [loading, setLoading] = useState(false);

  const ranked = useMemo(
    () =>
      environments
        .map((env) => ({ env, score: calcScore(env) }))
        .sort((a, b) => b.score - a.score),
    [environments]
  );

  const filtered = useMemo(() => {
    if (filter === 'all') return ranked;
    return ranked.filter(({ env }) => env.statusKey === STATUS_KEY_MAP[filter]);
  }, [ranked, filter]);

  const statusCounts = useMemo(() => ({
    normal: environments.filter((e) => e.statusKey === 'dashboard.statusNormal').length,
    warning: environments.filter((e) => e.statusKey === 'dashboard.statusWarning').length,
    alert: environments.filter((e) => e.statusKey === 'dashboard.statusAlert').length,
    total: environments.length,
  }), [environments]);

  const top3 = filtered.slice(0, 3);
  const rest = filtered.slice(3);

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      const [summary, seriesData] = await Promise.all([
        apiClient.get('/api/v1/dashboard/summary'),
        apiClient.get('/api/v1/dashboard/series', {
          params: { period: 'day', variable: 'co2' },
        }),
      ]);
      setDashboardData({ summary, series: seriesData });
      setSeries(seriesData);
    } catch (err) {
      console.error('Error fetching dashboard data:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchSeries = async (period, variable, environmentId) => {
    setLoading(true);
    try {
      const params = new URLSearchParams({ period, variable });
      if (environmentId && environmentId !== 'all') {
        params.set('environmentId', environmentId);
      }
      const data = await apiClient.get(`/api/v1/dashboard/series?${params.toString()}`);
      setSeries(data);
    } catch (err) {
      console.error('Error fetching dashboard series:', err);
    } finally {
      setLoading(false);
    }
  };

  return {
    filter,
    setFilter,
    filtered,
    top3,
    rest,
    statusCounts,
    toggleFavorite,
    dashboardData,
    series,
    loading,
    fetchDashboardData,
    fetchSeries,
  };
}
